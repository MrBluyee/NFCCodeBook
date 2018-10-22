package com.mrbluyee.nfccodebook.connectivity;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.mrbluyee.nfccodebook.utils.ArrayUtils;
import com.mrbluyee.nfccodebook.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class IsoDepClass {
    private Tag mTag = null;
    private IsoDep isoDep = null;
    private int tagCapabilityLength = 0;
    private int ccFileLength = 0;
    private byte mappingVersion = 0;
    private int tagMaxReadLength = 0;
    private int tagMaxWriteLength = 0;
    private byte tlvBlockValue = 0;
    private byte tlvBlockSize = 0;
    private int ndefFileLength = 0;
    private byte[] ndefFileID = null;
    private byte readAccess = 0;
    private byte writeAccess = 0;

    public IsoDepClass(Tag mTag){
        this.mTag = mTag;
        if(this.mTag != null) {
            isoDep = IsoDep.get(this.mTag);
            if(isoDep != null){
                try {
                    isoDep.connect();
                    if(isoDep.isConnected()) {
                        if(getCCMessage()){
                            getNDEFFileLen();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        isoDep.close();
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

    public int getCcFileLength() {
        return ccFileLength;
    }

    public int getNdefFileLength() {
        return ndefFileLength;
    }

    public int getTagMaxReadLength() {
        return tagMaxReadLength;
    }

    public int getTagMaxWriteLength() {
        return tagMaxWriteLength;
    }

    public byte getMappingVersion() {
        return mappingVersion;
    }

    public byte getReadAccess() {
        return readAccess;
    }

    public byte getTlvBlockSize() {
        return tlvBlockSize;
    }

    public byte getTlvBlockValue() {
        return tlvBlockValue;
    }

    public byte getWriteAccess() {
        return writeAccess;
    }

    public int getTagCapabilityLength() {
        return tagCapabilityLength;
    }

    public byte[] getNdefFileID() {
        return ndefFileID;
    }

    public IsoDep getIsoDep() {
        return isoDep;
    }

    public boolean selectAID()throws IOException{
        //00 A4 04 00 07 D2 76 00 00 85 01 01
        //90 00
        boolean status = false;
        byte[] cmd = {0x00,(byte) 0xa4,0x04,0x00,0x07,(byte) 0xd2,0x76,0x00,0x00,(byte) 0x85,0x01,0x01};
        byte[] ack = isoDep.transceive (cmd);
        if(ack[0] == -0x70 && ack[1] == 0x00){
            status = true;
        }
        return status;
    }

    public boolean selectCCFile()throws IOException {
        //00 A4 00 0C 02 E1 03
        //90 00
        boolean status = false;
        if(!selectAID()) return status;
        byte[] cmd = {0x00,(byte) 0xa4,0x00,0x0c,0x02,(byte) 0xe1,0x03};
        byte[] ack = isoDep.transceive (cmd);
        if(ack[0] == -0x70 && ack[1] == 0x00){
            status = true;
        }
        return status;
    }

    public boolean selectNDEFFile()throws IOException {
        //00 A4 00 0C 02
        //90 00
        boolean status = false;
        if(readCCFile() == null) return status;
        byte[] cmd = {0x00,(byte) 0xa4,0x00,0x0c,0x02};
        byte[] cmd2 = ArrayUtils.MergerArray(cmd,ndefFileID);
        byte[] ack = isoDep.transceive (cmd2);
        if(ack.length >= 2) {
            if (ack[0] == -0x70 && ack[1] == 0x00) {
                status = true;
            }
        }
        return status;
    }

    public boolean getCCMessage()throws IOException {
        boolean status = false;
        if(!selectCCFile()) return status;
        byte[] cmd = {0x00,(byte) 0xb0,0x00,0x00,0x02}; //getCCFileLength
        byte[] ack = isoDep.transceive (cmd);
        if(ack.length >= 4){
            if (ack[ack.length-2] == -0x70 && ack[ack.length-1] == 0x00) {
                ccFileLength = (ack[0] & 0xff) * 256 + (ack[1] & 0xff);
                status = true;
            }
        }
        if(status){
            status = false;
            byte[] ack1 = readFileBytes(0,ccFileLength);
            if(ack1.length == ccFileLength) {
                mappingVersion = ack1[2];
                tagMaxReadLength = (ack1[3] & 0xff) * 256 + (ack1[4] & 0xff);
                tagMaxWriteLength = (ack1[5] & 0xff) * 256 + (ack1[6] & 0xff);
                tlvBlockValue = ack1[7];
                tlvBlockSize = ack1[8];
                ndefFileID = ArrayUtils.SubArray(ack1,9,2);
                tagCapabilityLength = (ack1[11] & 0xff) * 256 + (ack1[12] & 0xff);
                readAccess = ack1[13];
                writeAccess = ack1[14];
                status = true;
            }
        }
        return status;
    }

    public boolean getNDEFFileLen()throws IOException {
        boolean status = false;
        byte[] cmd1 = {0x00,(byte) 0xa4,0x00,0x0c,0x02,(byte) 0xe1,0x04}; //selectNDEFFile
        byte[] ack1 = isoDep.transceive (cmd1);
        if(ack1.length >= 2) {
            if (ack1[0] == -0x70 && ack1[1] == 0x00) {
                status = true;
            }
        }
        if(status) {
            status = false;
            byte[] cmd2 = {0x00, (byte) 0xb0, 0x00, 0x00, 0x02};
            byte[] ack2 = isoDep.transceive(cmd2);
            if (ack2.length >= 4) {
                if (ack2[2] == -0x70 && ack2[3] == 0x00) {
                    ndefFileLength = (ack2[0] & 0xff) * 256 + (ack2[1] & 0xff);
                    status = true;
                }
            }
        }
        return status;
    }

    public byte[] readFileBytes(int index,int length)throws IOException {
        //00 B0 00 02
        byte[] cmd1 = {0x00,(byte) 0xb0};
        byte[] indexbyte = {(byte) (index >> 8 & 0xFF),(byte) (index & 0xFF)};
        byte lenbyte = (byte) (length & 0xFF);
        byte[] cmd2 = ArrayUtils.MergerArray(cmd1,indexbyte);
        byte[] cmd3 = ArrayUtils.MergerArray(cmd2,lenbyte);
        byte[] ack = isoDep.transceive (cmd3);
        if(ack.length > 2){
            if(ack[ack.length-2] == -0x70 && ack[ack.length-1] == 0x00){
                return ArrayUtils.SubArray(ack,0,ack.length-2);
            } else {
                return null;
            }
        }else {
            return null;
        }
    }

    public boolean writeNDEFBytes(int index,int length,byte[] datas)throws IOException {
        byte[] cmd1 = {0x00,(byte) 0xd6};
        byte[] indexbyte = {(byte) (index >> 8 & 0xFF),(byte) (index & 0xFF)};
        byte lengthbyte = (byte) length;
        byte[] cmd2 = ArrayUtils.MergerArray(cmd1,indexbyte);
        byte[] cmd3 = ArrayUtils.MergerArray(cmd2,lengthbyte);
        byte[] cmd4 = ArrayUtils.MergerArray(cmd3,datas);
        byte[] ack = isoDep.transceive (cmd4);
        if(ack.length >= 2) {
            if (ack[0] == -0x70 && ack[1] == 0x00) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean clearNDEFBytes(int index,int length)throws IOException {
        byte[] cmd1 = {0x00,(byte) 0xd6};
        byte[] indexbyte = {(byte) (index >> 8 & 0xFF),(byte) (index & 0xFF)};
        byte lengthbyte = (byte) length;
        byte[] datas = new byte[length];
        Arrays.fill(datas, (byte) 0);
        byte[] cmd2 = ArrayUtils.MergerArray(cmd1,indexbyte);
        byte[] cmd3 = ArrayUtils.MergerArray(cmd2,lengthbyte);
        byte[] cmd4 = ArrayUtils.MergerArray(cmd3,datas);
        byte[] ack = isoDep.transceive (cmd4);
        if(ack.length >= 2) {
            if (ack[0] == -0x70 && ack[1] == 0x00) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public byte[] readFileMessage(int index, int length)throws IOException {
        if(length == 0 || tagMaxReadLength == 0) return null;
        int times = length / tagMaxReadLength ;
        int remain = length % tagMaxReadLength;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(int i=0;i<times;i++){
            byte[] temp = readFileBytes(index + i * tagMaxReadLength, tagMaxReadLength);
            if(temp != null) {
                out.write(temp, 0, tagMaxReadLength);
            }
        }
        byte[] temp = readFileBytes(index + times * tagMaxReadLength, remain);
        if(temp != null) {
            out.write(temp, 0, remain);
        }
        return out.toByteArray();
    }

    public boolean writeNDEFMessage(byte[] datas)throws IOException {
        boolean result = true;
        int index = 2;
        ByteArrayInputStream in = new ByteArrayInputStream(datas);
        byte[] temp = new byte[this.tagMaxWriteLength];
        int n = 0;
        while((n = in.read(temp)) > 0) {
            result = writeNDEFBytes(index, n, temp);
            if(result == false) break;
            index += n;
        }
        return result;
    }

    public boolean clearNDEFMessage()throws IOException {
        boolean result = true;
        int index = 2;
        byte[] datas = new byte[ccFileLength];
        Arrays.fill(datas, (byte) 0);
        ByteArrayInputStream in = new ByteArrayInputStream(datas);
        byte[] temp = new byte[this.tagMaxWriteLength];
        int n = 0;
        while((n = in.read(temp)) > 0) {
            result = clearNDEFBytes(index, n);
            if(result == false) break;
            index += n;
        }
        return result;
    }

    public boolean writeFileLen(int length)throws IOException {
        //00 d6 00 00 02
        //90 00
        byte[] cmd = {0x00,(byte) 0xd6,0x00,0x00,0x02};
        byte[] lenbyte = {(byte) (length >> 8 & 0xFF),(byte) (length & 0xFF)};
        byte[] cmd1 = ArrayUtils.MergerArray(cmd,lenbyte);
        byte[] ack = isoDep.transceive (cmd1);
        if(ack.length >= 2) {
            if (ack[0] == -0x70 && ack[1] == 0x00) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public byte[] readCCFile()throws IOException {
        if(!selectCCFile()) return null;
        byte[] cmd1 = {(byte) 0x00,(byte) 0xb0,0x00,0x00};
        byte[] ack = readFileBytes(0,ccFileLength);
        return ack;
    }

    public byte[] readNDEFFile(){
        byte[] result = null;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                if(selectNDEFFile()) {
                    result = readFileMessage(2,ndefFileLength);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                isoDep.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean writeNDEFFile(byte[] datas){
        boolean result = false;
        if(datas.length > tagCapabilityLength) return result;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                if(selectNDEFFile()) {
                    if(writeNDEFMessage(datas)){
                        result = writeFileLen(datas.length);
                    }
                }
            }
        } catch (Exception e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        }finally {
            try {
                isoDep.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean clearNDEFFile(){
        boolean result = false;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                if(selectNDEFFile()) {
                    if(clearNDEFMessage()){
                        result = writeFileLen(0);
                    }
                }
            }
        } catch (Exception e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        }finally {
            try {
                isoDep.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
