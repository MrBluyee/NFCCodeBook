package com.mrbluyee.nfccodebook.specialTag;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import com.mrbluyee.nfccodebook.connectivity.IsoDepClass;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;

import java.io.IOException;

public class TNCOSTag extends IsoDepClass{
    private Tag mTag = null;
    private IsoDep isoDep = null;
    private byte[] defaultKey = {0x40,0x41,0x42,0x43,0x44,0x45,0x46,0x47};

    public TNCOSTag(Tag mTag) {
        super(mTag);
        this.mTag = mTag;
        isoDep = getIsoDep();
    }

    public boolean eraseNDEFMessage(byte[] key)throws IOException {
        //80 0E 00 00 08
        //90 00
        byte[] cmd = {(byte) 0x80,0x0e,0x00,0x00,0x08};
        byte[] cmd1 = ArrayUtils.MergerArray(cmd,key);
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

    private boolean changePINCommand(byte[] oldkey,byte[] newkey)throws IOException {
        byte[] cmd = {(byte) 0x80,0x24,0x00,0x00,0x10};
        byte[] cmd1 = ArrayUtils.MergerArray(cmd,oldkey);
        byte[] cmd2 = ArrayUtils.MergerArray(cmd1,newkey);
        byte[] ack = isoDep.transceive (cmd2);
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

    public boolean eraseNDEFFile(byte[] key){
        boolean result = false;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                if(selectNDEFFile()) {
                    result = eraseNDEFMessage(key);
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

    public boolean changePIN(byte[] oldkey,byte[] newkey){
        boolean result = false;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                if(selectAID()) {
                    result = changePINCommand(oldkey,newkey);
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

    public boolean writeNDEFFile(byte[] datas, byte[] key) {
        boolean result = false;
        if(datas.length > getTagCapabilityLength()) return result;
        try {
            isoDep.connect();
            if(isoDep.isConnected()) {
                result = eraseNDEFFile(key);
                if(result) {
                    result = false;
                    if (selectNDEFFile()) {
                        if (writeNDEFMessage(datas)) {
                            result = writeFileLen(datas.length);
                        }
                    }
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
}
