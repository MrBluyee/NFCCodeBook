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
import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class WriteToTagActivity extends Activity {
    protected NfcAdapter mNfcAdapter = null;
    protected PendingIntent mPendingIntent = null;
    protected Tag mTag;
    private String passwd;
    private CodeBook codeBook;
    private ImageView write_to_tag_imageview;
    private MyHandler myHandler;
    private Boolean clearTagFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_to_tag_view);
        initView();
        myHandler = new MyHandler(WriteToTagActivity.this);
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(packagename.equals(getPackageName())) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
                if (serializableHashMap != null) {
                    passwd = (String) bundle.get("key");
                    codeBook = new CodeBook(serializableHashMap.getMap());
                }
                Boolean clear_tag = (boolean) bundle.getBoolean("cleartag");
                if (clear_tag != null) {
                    clearTagFlag = clear_tag;
                }
            }
        }else{
            finish();
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
            if(clearTagFlag){
                new WritetoTagHandle(myHandler, mTag).new ClearTag().begin();
            }else {
                new WritetoTagHandle(myHandler, mTag).new WritetoTag(codeBook.convertRecordToString(), passwd).begin();
            }
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
            Intent intent;
            switch (msg.what) {
                case StatusCode.WRITETOTAGSUCCEED: //写入成功
                    Toast.makeText(context,"Write Successed", Toast.LENGTH_SHORT).show();
                    break;
                case StatusCode.WRITETOTAGFAILED: //写入失败
                    Toast.makeText(context,"Write Failed", Toast.LENGTH_SHORT).show();
                    break;
                case StatusCode.SPACENOTENOUGH: //空间不够
                    Toast.makeText(context,"Space not enough", Toast.LENGTH_SHORT).show();
                    break;
                case StatusCode.CLEARTAGSUCCEED: //清除成功
                    Toast.makeText(context,"Clear Successed", Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setPackage(getPackageName());
                    setResult(StatusCode.CLEARTAGSUCCEED, intent);//清除成功
                    break;
                case StatusCode.CLEARTAGFAILED: //清除失败
                    Toast.makeText(context,"Clear Failed", Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setPackage(getPackageName());
                    setResult(StatusCode.CLEARTAGFAILED, intent);//清除失败
                    break;
            }
            finish();
        }
    }
}
