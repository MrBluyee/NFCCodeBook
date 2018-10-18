package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.WritetoTagHandle;
import com.mrbluyee.nfccodebook.bean.CodeBook;
import com.mrbluyee.nfccodebook.adapter.ListViewAdapter;
import com.mrbluyee.nfccodebook.bean.CodeRecord;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private CodeBook codeBook;
    private List<String> recordList = new ArrayList<>();;
    private ListViewAdapter listViewAdapter;
    private ImageButton button_Add_Record;
    private ImageButton button_Save_Record;
    private ImageButton button_Change_Key;
    private String passwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
            if(savedInstanceState != null) {
                passwd = (String)bundle.get("key");
                codeBook = new CodeBook(serializableHashMap.getMap());
                initData();
            }else{
                passwd = (String)bundle.get("newkey");
                HashMap<String,CodeRecord> temp = new HashMap<String,CodeRecord>();
                codeBook = new CodeBook(temp);
            }
        }
    }

    private void initView(){
        listView = (ListView)findViewById(R.id.list_view);
        button_Add_Record = (ImageButton)findViewById(R.id.button_Add_Record);
        button_Save_Record = (ImageButton)findViewById(R.id.button_Save_Record);
        button_Change_Key = (ImageButton)findViewById(R.id.button_Change_Key);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListViewActivity.this,ListContentViewActivity.class);
                String recordName = (String) listViewAdapter.getItem(position);
                SerializableHashMap myMap = new SerializableHashMap();
                myMap.setMap(codeBook.book);
                Bundle bundle=new Bundle();
                bundle.putString("record",recordName);
                bundle.putSerializable("map",myMap);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,1);
            }
        });
        button_Add_Record.setOnClickListener(this);
        button_Save_Record.setOnClickListener(this);
        button_Change_Key.setOnClickListener(this);
    }

    private void initData(){
        if(codeBook != null) {
            Iterator<Map.Entry<String, CodeRecord>> iterator = this.codeBook.book.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CodeRecord> entry = iterator.next();
                String key = entry.getKey();
                recordList.add(key);
            }
        }
        listViewAdapter = new ListViewAdapter(this,recordList);
        listView.setAdapter(listViewAdapter);
        listView.setSelection(0);
    }

    private void updateListView(){
        recordList.clear();
        if(codeBook != null) {
            Iterator<Map.Entry<String, CodeRecord>> iterator = this.codeBook.book.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CodeRecord> entry = iterator.next();
                String key = entry.getKey();
                recordList.add(key);
            }
        }
        if(listViewAdapter != null){
            listViewAdapter.notifyDataSetChanged();
        }else{
            listViewAdapter = new ListViewAdapter(this,recordList);
            listView.setAdapter(listViewAdapter);
            listView.setSelection(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { //ListContentViewActivity
            if (resultCode == 2) {//数据有更新
                Bundle bundle = data.getBundleExtra("bundle");
                SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
                this.codeBook = new CodeBook(serializableHashMap.getMap());
                updateListView();
            }
        }else if(requestCode == 2){ //ChangePasswdActivity

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Add_Record:{
                Intent intent = new Intent(ListViewActivity.this,ListContentViewActivity.class);
                SerializableHashMap myMap = new SerializableHashMap();
                myMap.setMap(codeBook.book);
                Bundle bundle=new Bundle();
                bundle.putSerializable("map",myMap);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,1);
                break;
            }
            case R.id.button_Save_Record:{

                break;
            }
            case R.id.button_Change_Key:{
                Intent intent = new Intent(ListViewActivity.this,ChangePasswdActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("key",passwd);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,2);
                break;
            }
            default:
                break;
        }
    }
}
