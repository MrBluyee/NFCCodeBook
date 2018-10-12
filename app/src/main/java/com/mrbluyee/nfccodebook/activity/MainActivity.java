package com.mrbluyee.nfccodebook.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.ReadFromTagHandle;

public class MainActivity extends AppCompatActivity {
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
            Toast.makeText(this,"NFC not supported", Toast.LENGTH_SHORT);
            finish();
            return;
        } else {
            if (!mNfcAdapter.isEnabled()) {
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
            switch (msg.what) {
                case 1: //是一张处理过的卡
                    Intent intent = new Intent(MainActivity.this,DecodeActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putByteArray("tagID",mTag.getId());
                    bundle.putByteArray("tagContent",(byte[]) msg.obj);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                    break;
                case 2: //卡未处理过

                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(mTag != null) { //handle
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

