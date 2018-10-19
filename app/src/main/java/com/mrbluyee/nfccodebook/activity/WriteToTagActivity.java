package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.WritetoTagHandle;
import com.mrbluyee.nfccodebook.bean.CodeBook;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class WriteToTagActivity extends Activity {
    protected NfcAdapter mNfcAdapter = null;
    protected PendingIntent mPendingIntent = null;
    protected Tag mTag;
    private String passwd;
    private CodeBook codeBook;
    private ImageView write_to_tag_imageview;
    private MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_to_tag_view);
        initView();
        myHandler = new MyHandler(WriteToTagActivity.this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
            if (serializableHashMap != null) {
                passwd = (String) bundle.get("key");
                codeBook = new CodeBook(serializableHashMap.getMap());
            }
        }
    }

    private void initView(){
        write_to_tag_imageview = (ImageView)findViewById(R.id.write_to_tag_imageview);
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
                Toast.makeText(WriteToTagActivity.this,"please turn on the NFC",Toast.LENGTH_SHORT).show();
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }
        }
        mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(mTag != null) {
            new WritetoTagHandle(myHandler,mTag).new WritetoTag(codeBook.convertRecordToString(),passwd).begin();
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    class MyHandler extends Handler {
        private Context context;

        public MyHandler(Context context) {
            this.context = context;
        }
        public MyHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 4: //写入成功
                    Toast.makeText(context,"Write Successed", Toast.LENGTH_SHORT).show();
                    break;
                case 5: //写入失败
                    Toast.makeText(context,"Write Failed", Toast.LENGTH_SHORT).show();
                    break;
                case 6: //空间不够
                    Toast.makeText(context,"Space not enough", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
