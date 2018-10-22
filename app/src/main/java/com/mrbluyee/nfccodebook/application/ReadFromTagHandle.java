package com.mrbluyee.nfccodebook.application;

import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.connectivity.IsoDepClass;
import com.mrbluyee.nfccodebook.connectivity.MifareClassicClass;
import com.mrbluyee.nfccodebook.connectivity.MifareUltralightClass;
import com.mrbluyee.nfccodebook.utils.AESUtils;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;
import com.mrbluyee.nfccodebook.utils.GzipUtils;
import com.mrbluyee.nfccodebook.utils.StringUtils;

import java.io.EOFException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class ReadFromTagHandle {
    private final Handler mReadHandler;

    public ReadFromTagHandle(Handler mReadHandler){
        this.mReadHandler = mReadHandler;
    }

    public class FirstCheck implements Runnable {
        private Tag mTag;
        public FirstCheck(Tag mTag){
            this.mTag = mTag;
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
                    IsoDepClass isoDepClass = new IsoDepClass(mTag);
                    byte[] temp = isoDepClass.readNDEFFile();
                    if(temp != null) {
                        byte[] head = ArrayUtils.SubArray(temp, 0, tagID.length);
                        if (mReadHandler != null) {
                            if (Arrays.equals(head, tagID)) { //是一张处理过的卡
                                Message message = Message.obtain(mReadHandler, StatusCode.USEDCARD, ArrayUtils.SubArray(temp, tagID.length, temp.length - tagID.length));
                                message.sendToTarget();
                            } else { //卡未处理过
                                Message message = Message.obtain(mReadHandler, StatusCode.EMPTYCARD);
                                message.sendToTarget();
                            }
                        }
                    }else{ //卡未处理过
                        Message message = Message.obtain(mReadHandler, StatusCode.EMPTYCARD);
                        message.sendToTarget();
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareUltralight")){
                    MifareUltralightClass mifareUltralightClass = new MifareUltralightClass(mTag);
                    if(mifareUltralightClass.isHandledTagFlag()){
                        byte[] data = mifareUltralightClass.readFile();
                        Message message = Message.obtain(mReadHandler, StatusCode.USEDCARD, data);
                        message.sendToTarget();
                    }else{
                        Message message = Message.obtain(mReadHandler, StatusCode.EMPTYCARD);
                        message.sendToTarget();
                    }
                }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareClassic")){
                    MifareClassicClass mifareClassicClass = new MifareClassicClass(mTag);
                    if(mifareClassicClass.isHandledTagFlag()){
                        byte[] data = mifareClassicClass.readFile();
                        Message message = Message.obtain(mReadHandler, StatusCode.USEDCARD, data);
                        message.sendToTarget();
                    }else{
                        Message message = Message.obtain(mReadHandler, StatusCode.EMPTYCARD);
                        message.sendToTarget();
                    }
                }
            }
        }
    }

    public class ContentCheck implements Runnable {
        private byte[] data;
        private byte[] tagID;
        private String keyString;

        public ContentCheck(byte[] tagID,byte[] data,String keyString) {
            this.tagID = tagID;
            this.data = data;
            this.keyString = keyString;
        }

        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(data != null && keyString != null){
                byte[] temp = AESUtils.decrypt(data, keyString);
                if(temp != null){
                    byte[] head = ArrayUtils.SubArray(temp, 0, tagID.length);
                    if (mReadHandler != null) {
                        if (Arrays.equals(head, tagID)) { //解码正确
                            byte[] ugzipdata = ArrayUtils.SubArray(temp, tagID.length, temp.length - tagID.length);
                            String contentstr = GzipUtils.unCompress(ugzipdata);
                            Message message = Message.obtain(mReadHandler, StatusCode.DECODESUCCEED,contentstr);
                            message.sendToTarget();
                        } else { //解码错误
                            Message message = Message.obtain(mReadHandler, StatusCode.DECODEFAILED);
                            message.sendToTarget();
                        }
                    }
                }
            }
        }
    }
}
