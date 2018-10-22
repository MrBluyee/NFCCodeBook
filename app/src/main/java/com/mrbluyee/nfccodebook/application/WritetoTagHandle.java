package com.mrbluyee.nfccodebook.application;

import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.connectivity.IsoDepClass;
import com.mrbluyee.nfccodebook.connectivity.MifareClassicClass;
import com.mrbluyee.nfccodebook.connectivity.MifareUltralightClass;
import com.mrbluyee.nfccodebook.specialTag.TNCOSTag;
import com.mrbluyee.nfccodebook.utils.AESUtils;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;
import com.mrbluyee.nfccodebook.utils.GzipUtils;
import com.mrbluyee.nfccodebook.utils.StringUtils;

import java.util.Arrays;

public class WritetoTagHandle {
    private final Handler mReadHandler;
    private Tag mTag;

    public WritetoTagHandle(Handler mReadHandler,Tag mTag){
        this.mReadHandler = mReadHandler;
        this.mTag = mTag;
    }

    public class WritetoTag implements Runnable {
        private String data;
        private String key;

        public WritetoTag(String data,String key){
            this.data = data;
            this.key = key;
        }

        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(mTag != null) {
                String[] techList = mTag.getTechList();
                byte[] tagID = mTag.getId();
                if (Arrays.asList(techList).contains("android.nfc.tech.IsoDep")) {
                    TNCOSTag tncosTag = new TNCOSTag(mTag);
                    byte[] gzip_data = GzipUtils.compress(data);
                    byte[] encry_data = ArrayUtils.MergerArray(tagID,gzip_data);
                    byte[] encryed_data = AESUtils.encrypt(encry_data,key);
                    byte[] write_data = ArrayUtils.MergerArray(tagID,encryed_data);
                    int tagCapabilityLength = tncosTag.getTagCapabilityLength();
                    if(tagCapabilityLength > write_data.length) {
                        if(tncosTag.getWriteAccess() == 0x00){
                            if (tncosTag.writeNDEFFile(write_data)) { //写入成功
                                Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGSUCCEED);
                                message.sendToTarget();
                            } else { //写入失败
                                Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGFAILED);
                                message.sendToTarget();
                            }
                        }else {
                            if (tncosTag.writeNDEFFileWithKey(write_data, tncosTag.defaultKey)) { //写入成功
                                Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGSUCCEED);
                                message.sendToTarget();
                            } else { //写入失败
                                Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGFAILED);
                                message.sendToTarget();
                            }
                        }
                    }else { //空间不够
                        Message message = Message.obtain(mReadHandler, StatusCode.SPACENOTENOUGH);
                        message.sendToTarget();
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareUltralight")){
                    MifareUltralightClass mifareUltralightClass = new MifareUltralightClass(mTag);
                    byte[] gzip_data = GzipUtils.compress(data);
                    byte[] encry_data = ArrayUtils.MergerArray(tagID,gzip_data);
                    byte[] encryed_data = AESUtils.encrypt(encry_data,key);
                    int tagCapabilityLength = mifareUltralightClass.getTagCapabilityLength();
                    if(tagCapabilityLength > encryed_data.length + 9){
                        if (mifareUltralightClass.writeFile(encryed_data)) { //写入成功
                            Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGSUCCEED);
                            message.sendToTarget();
                        } else { //写入失败
                            Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGFAILED);
                            message.sendToTarget();
                        }
                    }else { //空间不够
                        Message message = Message.obtain(mReadHandler, StatusCode.SPACENOTENOUGH);
                        message.sendToTarget();
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareClassic")){
                    MifareClassicClass mifareClassicClass = new MifareClassicClass(mTag);
                    byte[] gzip_data = GzipUtils.compress(data);
                    byte[] encry_data = ArrayUtils.MergerArray(tagID,gzip_data);
                    byte[] encryed_data = AESUtils.encrypt(encry_data,key);
                    int tagCapabilityLength = mifareClassicClass.getTagCapabilityLength();
                    if(tagCapabilityLength > encryed_data.length + 6){
                        if (mifareClassicClass.writeFile(encryed_data)) { //写入成功
                            Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGSUCCEED);
                            message.sendToTarget();
                        } else { //写入失败
                            Message message = Message.obtain(mReadHandler, StatusCode.WRITETOTAGFAILED);
                            message.sendToTarget();
                        }
                    }else { //空间不够
                        Message message = Message.obtain(mReadHandler, StatusCode.SPACENOTENOUGH);
                        message.sendToTarget();
                    }
                }
            }
        }
    }

    public class ClearTag implements Runnable {
        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(mTag != null) {
                String[] techList = mTag.getTechList();
                byte[] tagID = mTag.getId();
                if (Arrays.asList(techList).contains("android.nfc.tech.IsoDep")) {
                    TNCOSTag tncosTag = new TNCOSTag(mTag);
                    if(tncosTag.getWriteAccess() == 0x00){
                        if (tncosTag.clearNDEFFile()) { //清除成功
                            Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGSUCCEED);
                            message.sendToTarget();
                        } else { //清除失败
                            Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGFAILED);
                            message.sendToTarget();
                        }
                    }else {
                        if (tncosTag.clearNDEFFileWithKey(tncosTag.defaultKey)) { //清除成功
                            Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGSUCCEED);
                            message.sendToTarget();
                        } else { //清除失败
                            Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGFAILED);
                            message.sendToTarget();
                        }
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareUltralight")){
                    MifareUltralightClass mifareUltralightClass = new MifareUltralightClass(mTag);
                    if(mifareUltralightClass.clearFile()){ //清除成功
                        Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGSUCCEED);
                        message.sendToTarget();
                    } else { //清除失败
                        Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGFAILED);
                        message.sendToTarget();
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareClassic")){
                    MifareClassicClass mifareClassicClass = new MifareClassicClass(mTag);
                    if(mifareClassicClass.clearFile()){ //清除成功
                        Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGSUCCEED);
                        message.sendToTarget();
                    } else { //清除失败
                        Message message = Message.obtain(mReadHandler, StatusCode.CLEARTAGFAILED);
                        message.sendToTarget();
                    }
                }
            }
        }
    }
}
