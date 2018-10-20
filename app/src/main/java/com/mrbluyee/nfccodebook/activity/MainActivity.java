package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.ReadFromTagHandle;
import com.mrbluyee.nfccodebook.bean.StatusCode;

public class MainActivity extends Activity {
    protected static final String activityTAG = MainActivity.class.getName();
    protected NfcAdapter mNfcAdapter = null;
    protected PendingIntent mPendingIntent = null;
    protected Tag mTag;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHandler = new MyHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this,"NFC not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Toast.makeText(MainActivity.this,"please turn on the NFC",Toast.LENGTH_SHORT).show();
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }
        }
        mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }
        public MyHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case StatusCode.USEDCARD: //是一张处理过的卡
                    intent = new Intent(MainActivity.this,DecodeActivity.class);
                    bundle = new Bundle();
                    bundle.putByteArray("tagID",mTag.getId());
                    bundle.putByteArray("tagContent",(byte[]) msg.obj);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                    break;
                case StatusCode.EMPTYCARD: //卡未处理过
                    intent = new Intent(MainActivity.this,TagInfoActivity.class);
                    intent.putExtra("mtag",mTag);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(mTag != null) {
            Toast.makeText(MainActivity.this,"Tag detected",Toast.LENGTH_SHORT).show();
            new ReadFromTagHandle(myHandler).new FirstCheck(mTag).begin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null){
            mNfcAdapter.enableForegroundDispatch(this,mPendingIntent,null,null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null){
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}

