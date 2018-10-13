package com.mrbluyee.nfccodebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.CodeBook;
import com.mrbluyee.nfccodebook.application.ListViewAdapter;
import com.mrbluyee.nfccodebook.utils.CodeRecord;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private CodeBook codeBook;
    private List<String> recordList = new ArrayList<>();;
    private ListViewAdapter listViewAdapter;
    private Button button_Add_Record;
    private Button button_Save_Record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
            this.codeBook = new CodeBook(serializableHashMap.getMap());
            initData();
        }else{
            HashMap<String,CodeRecord> temp = new HashMap<String,CodeRecord>();
            this.codeBook = new CodeBook(temp);
        }
    }

    private void initView(){
        listView = (ListView)findViewById(R.id.list_view);
        button_Add_Record = (Button)findViewById(R.id.button_Add_Record);
        button_Save_Record = (Button)findViewById(R.id.button_Save_Record);
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
        button_Add_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewActivity.this,ListContentViewActivity.class);
                startActivityForResult(intent,1);
            }
        });
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
        if(requestCode == 1) {
            if (resultCode == 2) {//数据有更新
                Bundle bundle = data.getBundleExtra("bundle");
                SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
                this.codeBook = new CodeBook(serializableHashMap.getMap());
                updateListView();
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

    @Override
    public void onClick(View v) {

    }
}
