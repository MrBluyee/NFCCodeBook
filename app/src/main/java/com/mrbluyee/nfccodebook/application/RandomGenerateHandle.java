package com.mrbluyee.nfccodebook.application;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.ArrayUtils;
import com.mrbluyee.nfccodebook.utils.ScreenUtils;
import com.mrbluyee.nfccodebook.utils.StringUtils;

public class RandomGenerateHandle {
    private final Handler mReadHandler;

    public RandomGenerateHandle(Handler mReadHandler){
        this.mReadHandler = mReadHandler;
    }

    public class RandomGenerate implements Runnable {
        private int form_count;
        private int length;

        public RandomGenerate(int form_count,int length){
            this.form_count = form_count;
            this.length = length;
        }

        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(form_count>0 && length > 0) {
                String str_temp = StringUtils.getRandomString(form_count, length);
                Message message = Message.obtain(mReadHandler, StatusCode.PASSWDCREATED, str_temp);
                message.sendToTarget();
            }
        }
    }
}
