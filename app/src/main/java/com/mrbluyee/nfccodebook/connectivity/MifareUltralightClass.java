package com.mrbluyee.nfccodebook.connectivity;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import com.mrbluyee.nfccodebook.BuildConfig;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class MifareUltralightClass {
    private static final boolean isDebug = BuildConfig.DEBUG;
    private Tag mTag = null;
    private MifareUltralight mfu;
    private int tagMaxTransceiveLength = 0;
    private int tagType;
    private int tagCapabilityLength = 0;
    private int fileLength = 0;
    private byte mappingVersion = 0;
    private byte readAccess = 0;
    private byte writeAccess = 0;
    private byte nfcForumDefine;
    private boolean isHandledTagFlag = false;

    public MifareUltralightClass(Tag mTag){
        this.mTag = mTag;
        if(this.mTag != null) {
            mfu = MifareUltralight.get(this.mTag);
            if(mfu != null){
                try {
                    mfu.connect();
                    if(mfu.isConnected()){
                        getCCMessage();
                        getFileMessage();
                    }
                } catch (Exception e) {
                    if (isDebug) {
                        e.printStackTrace();
                    }
                }finally {
                    try {
                        mfu.close();
                    } catch (IOException e) {
                        if (isDebug) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public int getTagMaxTransceiveLength(){
        return tagMaxTransceiveLength;
    }

    public int getTagCapabilityLength() {
        return tagCapabilityLength;
    }

    public byte getReadAccess() {
        return readAccess;
    }

    public byte getMappingVersion() {
        return mappingVersion;
    }

    public int getFileLength() {
        return fileLength;
    }

    public byte getWriteAccess() {
        return writeAccess;
    }

    public int getTagType() {
        return tagType;
    }

    public boolean isHandledTagFlag() {
        return isHandledTagFlag;
    }

    public byte[] getId(){
        if(mTag != null) {
            return mTag.getId();
        }else {
            return null;
        }
    }

    public boolean getCCMessage()throws IOException {
        boolean status = false;
        tagMaxTransceiveLength = mfu.getMaxTransceiveLength();
        tagType = mfu.getType();
        byte[] result =  mfu.readPages(0);
        if(result.length == 16){
            status = true;
            nfcForumDefine = result[12];
            if(nfcForumDefine == (byte)0xe1){
                mappingVersion = result[13];
                tagCapabilityLength = (result[14] & 0xff) * 8;
                readAccess = (byte) (result[15] & 0xf0);
                writeAccess = (byte) (result[15] & 0x0f);
            }
        }
        return status;
    }

    public boolean getFileMessage()throws IOException {
        boolean status = false;
        byte[] result =  mfu.readPages(4);
        if(result.length == 16){
            status = true;
            byte[] fileFlagCheck = ArrayUtils.SubArray(result,2,7);
            if(Arrays.equals(fileFlagCheck, getId())){
                isHandledTagFlag = true;
                byte[] fileLengthByte = ArrayUtils.SubArray(result,0,2);
                fileLength = (fileLengthByte [0] & 0xff) * 256 + (fileLengthByte [1] & 0xff);
            }else {
                isHandledTagFlag = false;
                fileLength = 0;
            }
        }
        return status;
    }

    public byte[] readDatas(int startblock, int endblock)throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int times = (endblock - startblock) / 4;
        int remain = (endblock - startblock) % 4;
        for(int i=0;i<times;i++){
            byte[] result = mfu.readPages(startblock + i * 4);
            out.write(result, 0, result.length);
        }
        if(remain > 0){
            byte[] result = mfu.readPages(startblock + times * 4);
            byte[] remainBytes = ArrayUtils.SubArray(result,0,remain * 4);
            out.write(remainBytes, 0, remainBytes.length);
        }
        return out.toByteArray();
    }

    public boolean writedatas(byte[] datas)throws IOException {
        boolean status = false;
        int data_length = datas.length;
        byte[] data_length_byte = {(byte) (data_length >> 8 & 0xFF),(byte) (data_length & 0xFF)};
        byte[] write_head = ArrayUtils.MergerArray(data_length_byte,getId());
        byte[] write_data = ArrayUtils.MergerArray(write_head,datas);
        if(write_data.length < tagCapabilityLength) {
            status = true;
            ByteArrayInputStream in = new ByteArrayInputStream(write_data);
            byte[] buffer = new byte[4];
            int n = -1;
            int i = 4;
            while((n = in.read(buffer)) > 0) {
                mfu.writePage(i++,buffer);
            }
        }
        return status;
    }

    public boolean clearDatas()throws IOException {
        byte[] data = new byte[fileLength + 9];
        Arrays.fill(data, (byte) 0);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[4];
        int n = -1;
        int i = 4;
        while((n = in.read(buffer)) > 0) {
            mfu.writePage(i++,buffer);
        }
        return true;
    }

    public byte[] readFile(){
        int dataStartBlock = 6;
        byte[] result = null;
        if(isHandledTagFlag) {
            if(fileLength > 3) {
                int file_byte_last = fileLength - 3;
                int block_count = file_byte_last / 4 + 1;
                int file_byte_remain = file_byte_last % 4;
                if(file_byte_remain > 0){
                    block_count ++;
                }
                try {
                    mfu.connect();
                    if (mfu.isConnected()) {
                        byte[] temp = readDatas(dataStartBlock, dataStartBlock + block_count);
                        if(temp != null){
                            result = ArrayUtils.SubArray(temp ,1,fileLength);
                        }
                    }
                } catch (Exception e) {
                    if (isDebug) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        mfu.close();
                    } catch (IOException e) {
                        if (isDebug) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean writeFile(byte[] datas){
        boolean status = false;
        try {
            mfu.connect();
            if (mfu.isConnected()) {
                status = writedatas(datas);
            }
        } catch (Exception e) {
            if (isDebug) {
                if(!(e instanceof EOFException)){
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                mfu.close();
            } catch (IOException e) {
                if (isDebug) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public boolean clearFile(){
        boolean status = false;
        try {
            mfu.connect();
            if (mfu.isConnected()) {
                status = clearDatas();
            }
        } catch (Exception e) {
            if (isDebug) {
                if(!(e instanceof EOFException)){
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                mfu.close();
            } catch (IOException e) {
                if (isDebug) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }
}
