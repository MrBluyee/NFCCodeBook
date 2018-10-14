package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.bean.CodeBook;
import com.mrbluyee.nfccodebook.application.ReadFromTagHandle;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class DecodeActivity extends Activity {
    private byte[] tagContent = null;
    private byte[] tagID = null;
    private EditText editText_Decode_passwd;
    private Button button_Decode_Enter;
    private MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_view);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        tagID = bundle.getByteArray("tagID");
        tagContent = bundle.getByteArray("tagContent");
        initView();
        myHandler = new MyHandler();
    }

    private void initView(){
        editText_Decode_passwd = (EditText)findViewById(R.id.editText_Decode_passwd);
        button_Decode_Enter = (Button)findViewById(R.id.button_Decode_Enter);
        button_Decode_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd = editText_Decode_passwd.getText().toString();
                if(passwd != null){
                    new ReadFromTagHandle(myHandler).new ContentCheck(tagID,tagContent,passwd).begin();
                }
            }
        });
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
                case 1: //解码正确
                    String contentstr = (String) msg.obj;
                    CodeBook codeBook = new CodeBook(contentstr);
                    if(codeBook.book != null) {
                        Intent intent = new Intent(DecodeActivity.this, ListViewActivity.class);
                        Bundle bundle = new Bundle();
                        SerializableHashMap myMap = new SerializableHashMap();
                        myMap.setMap(codeBook.book);
                        bundle.putSerializable("map",myMap);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(DecodeActivity.this,"broken file!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2: //解码错误
                    Toast.makeText(DecodeActivity.this,"wrong password!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
