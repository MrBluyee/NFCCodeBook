package com.mrbluyee.nfccodebook.application;

import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;

import com.mrbluyee.nfccodebook.connectivity.IsoDepClass;
import com.mrbluyee.nfccodebook.specialTag.TNCOSTag;
import com.mrbluyee.nfccodebook.utils.AESUtils;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;
import com.mrbluyee.nfccodebook.utils.GzipUtils;

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
                        if (tncosTag.writeNDEFFile(write_data, tncosTag.defaultKey)) { //写入成功
                            Message message = Message.obtain(mReadHandler, 4);
                            message.sendToTarget();
                        } else { //写入失败
                            Message message = Message.obtain(mReadHandler, 5);
                            message.sendToTarget();
                        }
                    }else { //空间不够
                        Message message = Message.obtain(mReadHandler, 6);
                        message.sendToTarget();
                    }
                }
            }
        }
    }
}
