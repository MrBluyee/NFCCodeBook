package com.mrbluyee.nfccodebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.CodeBook;
import com.mrbluyee.nfccodebook.utils.CodeRecord;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class ListContentViewActivity extends AppCompatActivity {
    private CodeBook codeBook;
    private String recordName;
    private CodeRecord codeRecord;
    private boolean dataChanged = false;

    private EditText editText_Record;
    private EditText editText_Account;
    private EditText editText_Password;
    private EditText editText_Remark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_content_view);
        initView();
        Intent intent = getIntent();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
        this.codeBook = new CodeBook(serializableHashMap.getMap());
        this.recordName = bundle.getString("record");
        this.codeRecord = this.codeBook.book.get(this.recordName);
        editText_Record.setText(this.recordName);
        editText_Account.setText(this.codeRecord.account);
        editText_Password.setText(this.codeRecord.password);
        editText_Remark.setText(this.codeRecord.remark);
    }

    private void initView(){
        editText_Record = (EditText)findViewById(R.id.editText_Record);
        editText_Account = (EditText)findViewById(R.id.editText_Account);
        editText_Password = (EditText)findViewById(R.id.editText_Password);
        editText_Remark = (EditText)findViewById(R.id.editText_Remark);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if(dataChanged){
            SerializableHashMap myMap = new SerializableHashMap();
            myMap.setMap(codeBook.book);
            Bundle bundle=new Bundle();
            bundle.putSerializable("map",myMap);
            intent.putExtra("bundle",bundle);
            setResult(2, intent);//数据更新
        }else{
            setResult(3, intent);//数据未更新
        }
        super.onBackPressed();
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
