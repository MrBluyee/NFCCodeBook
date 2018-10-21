package com.mrbluyee.nfccodebook.connectivity;

import android.nfc.FormatException;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;

import com.mrbluyee.nfccodebook.utils.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class MifareClassicClass{
    private Tag mTag = null;
    private MifareClassic mfc = null;
    private byte[] tag_id;
    private int blockCount;
    private int sectorCount;
    private int tag_size;
    private int tag_type;
    private int tagCapabilityLength = 0;
    private int fileLength = 0;
    private byte readAccess = 0;
    private byte writeAccess = 0;
    private int tagMaxTransceiveLength = 0;
    private boolean isHandledTagFlag = false;

    public MifareClassicClass(Tag mTag){
        this.mTag = mTag;
        if(this.mTag != null) {
            mfc = MifareClassic.get(this.mTag);
            if(mfc != null){
                try {
                    mfc.connect();
                    if(mfc.isConnected()) {
                        tag_id = getId();
                        blockCount = mfc.getBlockCount();
                        sectorCount = mfc.getSectorCount();
                        tag_size = mfc.getSize();
                        tag_type = mfc.getType();
                        tagMaxTransceiveLength = mfc.getMaxTransceiveLength();
                        if (tag_size == MifareClassic.SIZE_1K) {
                            tagCapabilityLength = 752;
                        } else if (tag_size == MifareClassic.SIZE_2K) {
                            tagCapabilityLength = 1520;
                        } else if (tag_size == MifareClassic.SIZE_4K) {
                            tagCapabilityLength = 3440;
                        }
                        getFileMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        mfc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public byte[] getId(){
        if(mTag != null) {
            return mTag.getId();
        }else {
            return null;
        }
    }

    public boolean isHandledTagFlag() {
        return isHandledTagFlag;
    }

    public int getFileLength() {
        return fileLength;
    }

    public int getTagCapabilityLength() {
        return tagCapabilityLength;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public int getSectorCount() {
        return sectorCount;
    }

    public int getTag_size() {
        return tag_size;
    }

    public int getTagMaxTransceiveLength() {
        return tagMaxTransceiveLength;
    }

    public int getTag_type() {
        return tag_type;
    }

    public byte getWriteAccess() {
        return writeAccess;
    }

    public byte getReadAccess() {
        return readAccess;
    }

    public int getSectorCapabilityLength(int sector)throws IOException{
        int block_count = mfc.getBlockCountInSector(sector);
        if(sector == 0){
            return (block_count - 2) * 16;
        }else {
            return (block_count - 1) * 16;
        }
    }

    public byte[] readSectorBytes(int sector)throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean status = false;
        int block_count = mfc.getBlockCountInSector(sector);
        if(sector == 0){
            block_count -= 2;
        }else {
            block_count -= 1;
        }
        if(mfc.authenticateSectorWithKeyA(sector,MifareClassic.KEY_DEFAULT)){
            int start_block = mfc.sectorToBlock(sector);
            if(sector == 0) start_block++;
            for(int i=0;i<block_count;i++){
                byte[] buffer = mfc.readBlock(start_block + i);
                out.write(buffer, 0, buffer.length);
            }
        }
        return out.toByteArray();
    }

    public boolean getFileMessage()throws IOException {
        boolean status = false;
        byte[] result = readSectorBytes(0);
        if(result.length == 32){
            status = true;
            writeAccess = 0;
            readAccess = 0;
            byte[] fileFlagCheck = ArrayUtils.SubArray(result,2,4);
            if(Arrays.equals(fileFlagCheck, tag_id)){
                isHandledTagFlag = true;
                byte[] fileLengthByte = ArrayUtils.SubArray(result,0,2);
                fileLength = (fileLengthByte [0] & 0xff) * 0xff + (fileLengthByte [1] & 0xff);
            }else {
                isHandledTagFlag = false;
                fileLength = 0;
            }
        }else{
            writeAccess = 0x0f;
            readAccess = 0x0f;
        }
        return status;
    }

    public boolean writeSectorBytes(int sector, byte[] data)throws IOException{
        boolean status = false;
        int block_count = mfc.getBlockCountInSector(sector);
        int sectorCapabilityLength;
        if(sector == 0){
            sectorCapabilityLength = (block_count - 2) * 16;
        }else {
            sectorCapabilityLength = (block_count - 1) * 16;
        }
        if(mfc.authenticateSectorWithKeyA(sector,MifareClassic.KEY_DEFAULT)){
            if(sectorCapabilityLength >= data.length){
                status = true;
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                byte[] buffer = new byte[16];
                int n = -1;
                int start_block = mfc.sectorToBlock(sector);
                if(sector == 0) start_block++;
                while ((n = in.read(buffer)) > 0){
                    mfc.writeBlock (start_block++, buffer);
                }
            }
        }
        return status;
    }

    public boolean clearSector(int sector)throws IOException {
        boolean status = false;
        int block_count = mfc.getBlockCountInSector(sector);
        if(sector == 0){
            block_count -= 2;
        }else {
            block_count -= 1;
        }
        if(mfc.authenticateSectorWithKeyA(sector,MifareClassic.KEY_DEFAULT)){
            status = true;
            byte[] buffer = new byte[16];
            Arrays.fill(buffer, (byte) 0);
            int start_block = mfc.sectorToBlock(sector);
            if(sector == 0) start_block++;
            for(int i=0;i<block_count;i++) {
                mfc.writeBlock(start_block+i, buffer);
            }
        }
        return status;
    }

    public boolean writeBytes(byte[] datas)throws IOException{
        boolean status = false;
        int data_length = datas.length;
        byte[] data_length_byte = {(byte) (data_length >> 8 & 0xFF),(byte) (data_length & 0xFF)};
        byte[] write_head = ArrayUtils.MergerArray(data_length_byte,getId());
        byte[] write_data = ArrayUtils.MergerArray(write_head,datas);
        if(tagCapabilityLength > write_data.length){
            ByteArrayInputStream in = new ByteArrayInputStream(write_data);
            int sector_index = 0;
            int n = -1;
            do {
                if(sector_index < sectorCount) {
                    byte[] buffer = new byte[getSectorCapabilityLength(sector_index)];
                    n = in.read(buffer);
                    if(n>0) {
                        status = writeSectorBytes(sector_index++, buffer);
                        if(status == false) break;
                    }
                }else{
                    break;
                }
            }
            while (n>0);
        }
        return status;
    }

    public byte[] readFile(){
        byte[] result = null;
        if(isHandledTagFlag) {
            if(fileLength > 0) {
                try {
                    mfc.connect();
                    if (mfc.isConnected()) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        int file_remain = fileLength + 6;
                        int sector_index = 0;
                        int n = -1;
                        do {
                            if(sector_index < sectorCount) {
                                byte[] buffer = readSectorBytes(sector_index++);
                                out.write(buffer, 0, buffer.length);
                                file_remain -= buffer.length;
                            }else {
                                break;
                            }
                        }
                        while (file_remain > 0);
                        byte[] temp = out.toByteArray();
                        if(temp != null){
                            result = ArrayUtils.SubArray(temp ,6,fileLength);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        mfc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public boolean writeFile(byte[] datas){
        boolean status = false;
        try {
            mfc.connect();
            if (mfc.isConnected()) {
                status = writeBytes(datas);
            }
        } catch (Exception e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    public boolean clearFile(){
        boolean status = false;
        try {
            mfc.connect();
            if (mfc.isConnected()) {
                int file_remain = fileLength + 6;
                int sector_index = 0;
                do {
                    if(sector_index < sectorCount) {
                        int sector_length = getSectorCapabilityLength(sector_index);
                        status = clearSector(sector_index++);
                        if (status == false) break;
                        file_remain -= sector_length;
                    }else{
                        break;
                    }
                }while (file_remain > 0);
            }
        } catch (Exception e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
}