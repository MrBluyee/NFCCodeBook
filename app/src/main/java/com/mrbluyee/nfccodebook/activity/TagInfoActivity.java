package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.adapter.TagInfoAdapter;
import com.mrbluyee.nfccodebook.adapter.TreeAdapter;
import com.mrbluyee.nfccodebook.bean.TreeItem;
import com.mrbluyee.nfccodebook.connectivity.IsoDepClass;
import com.mrbluyee.nfccodebook.connectivity.MifareClassicClass;
import com.mrbluyee.nfccodebook.connectivity.MifareUltralightClass;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;
import com.mrbluyee.nfccodebook.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagInfoActivity extends Activity {
    protected static final String activityTAG = TagInfoActivity.class.getName();
    private Tag mTag;
    String[] techList;
    private IsoDepClass isoDepClass = null;
    private MifareUltralightClass mifareUltralightClass = null;
    private MifareClassicClass mifareClassicClass = null;
    private ListView taginfo_list_view;
    private ImageButton button_Create_CodeBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_info_view);
        initView();
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(packagename.equals(getPackageName())) {
            mTag = intent.getParcelableExtra("mtag");
        }else{
            finish();
        }
        if(mTag != null) {
            techList = mTag.getTechList();
            if (Arrays.asList(techList).contains("android.nfc.tech.IsoDep")) {
                isoDepClass = new IsoDepClass(mTag);
                initIsoDepData();
            }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareUltralight")){
                mifareUltralightClass = new MifareUltralightClass(mTag);
                initMifareUltralightData();
            }else if(Arrays.asList(techList).contains("android.nfc.tech.MifareClassic")){
                mifareClassicClass = new MifareClassicClass(mTag);
                initMifareClassicData();
            }
        }
    }

    private void initView(){
        taginfo_list_view = (ListView)findViewById(R.id.taginfo_list_view);
        button_Create_CodeBook = (ImageButton)findViewById(R.id.button_Create_CodeBook);
        button_Create_CodeBook.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(TagInfoActivity.this, CreatePasswdActivity.class);
                  intent.setPackage(getPackageName());
                  startActivity(intent);
              }
            }
        );
    }

    private void initIsoDepData(){
        final List<TreeItem> list = new ArrayList<>();
        list.add(new TreeItem(0, 0, 0, true, "Tag ID"));
        list.add(new TreeItem(1, 0, 0, true, "Tech"));
        list.add(new TreeItem(2, 0, 0, true, "Capability"));
        list.add(new TreeItem(3, 0, 0, true, "MaxReadLength"));
        list.add(new TreeItem(4, 0, 0, true, "MaxWriteLength"));
        list.add(new TreeItem(5, 0, 0, true, "readAccess"));
        list.add(new TreeItem(6, 0, 0, true, "writeAccess"));
        list.add(new TreeItem(7, 0, 1, true, StringUtils.bytesToHexString(mTag.getId())));
        if(isoDepClass != null){
            list.add(new TreeItem(8, 2, 1, true, ""+ isoDepClass.getTagCapabilityLength() +"bytes"));
            list.add(new TreeItem(9, 3, 1, true, ""+ isoDepClass.getTagMaxReadLength() +"bytes"));
            list.add(new TreeItem(10, 4, 1, true, ""+ isoDepClass.getTagMaxWriteLength() +"bytes"));
            if(isoDepClass.getReadAccess() == 0x00){
                list.add(new TreeItem(11, 5, 1, true, "Read free"));
            }else{
                list.add(new TreeItem(11, 5, 1, true, "Read protected"));
            }
            if(isoDepClass.getWriteAccess() == 0x00){
                list.add(new TreeItem(12, 6, 1, true, "Write free"));
            }else{
                list.add(new TreeItem(12, 6, 1, true, "Write protected"));
            }
            for(int i=0;i<techList.length;i++){
                list.add(new TreeItem(13 + i, 1, 1, true, techList[i]));
            }
        }

        final TagInfoAdapter adapter = new TagInfoAdapter(list,this);
        adapter.setOnInnerItemClickListener(new TreeAdapter.OnInnerItemClickListener<TreeItem>() {
            @Override
            public void onClick(TreeItem node) {
                Log.i(activityTAG , "click: " + node.name);
            }
        });
        adapter.setOnInnerItemLongClickListener(new TreeAdapter.OnInnerItemLongClickListener<TreeItem>() {
            @Override
            public void onLongClick(TreeItem node) {
                Log.i(activityTAG , "long click: " + node.name);
            }
        });
        taginfo_list_view.setAdapter(adapter);
    }

    private void initMifareUltralightData(){
        final List<TreeItem> list = new ArrayList<>();
        list.add(new TreeItem(0, 0, 0, true, "Tag ID"));
        list.add(new TreeItem(1, 0, 0, true, "Tech"));
        list.add(new TreeItem(2, 0, 0, true, "Capability"));
        list.add(new TreeItem(3, 0, 0, true, "MaxReadLength"));
        list.add(new TreeItem(4, 0, 0, true, "MaxWriteLength"));
        list.add(new TreeItem(5, 0, 0, true, "readAccess"));
        list.add(new TreeItem(6, 0, 0, true, "writeAccess"));
        list.add(new TreeItem(7, 0, 1, true, StringUtils.bytesToHexString(mTag.getId())));
        if(mifareUltralightClass != null){
            list.add(new TreeItem(8, 2, 1, true, ""+ mifareUltralightClass.getTagCapabilityLength() +"bytes"));
            list.add(new TreeItem(9, 3, 1, true, ""+ mifareUltralightClass.getTagMaxTransceiveLength() +"bytes"));
            list.add(new TreeItem(10, 4, 1, true, ""+ mifareUltralightClass.getTagMaxTransceiveLength() +"bytes"));
            if(mifareUltralightClass.getReadAccess() == 0x00){
                list.add(new TreeItem(11, 5, 1, true, "Read free"));
            }else{
                list.add(new TreeItem(11, 5, 1, true, "Read protected"));
            }
            if(mifareUltralightClass.getWriteAccess() == 0x00){
                list.add(new TreeItem(12, 6, 1, true, "Write free"));
            }else{
                list.add(new TreeItem(12, 6, 1, true, "Write protected"));
            }
            for(int i=0;i<techList.length;i++){
                list.add(new TreeItem(13 + i, 1, 1, true, techList[i]));
            }
        }

        final TagInfoAdapter adapter = new TagInfoAdapter(list,this);
        adapter.setOnInnerItemClickListener(new TreeAdapter.OnInnerItemClickListener<TreeItem>() {
            @Override
            public void onClick(TreeItem node) {
                Log.i(activityTAG , "click: " + node.name);
            }
        });
        adapter.setOnInnerItemLongClickListener(new TreeAdapter.OnInnerItemLongClickListener<TreeItem>() {
            @Override
            public void onLongClick(TreeItem node) {
                Log.i(activityTAG , "long click: " + node.name);
            }
        });
        taginfo_list_view.setAdapter(adapter);
    }

    private void initMifareClassicData(){
        final List<TreeItem> list = new ArrayList<>();
        list.add(new TreeItem(0, 0, 0, true, "Tag ID"));
        list.add(new TreeItem(1, 0, 0, true, "Tech"));
        list.add(new TreeItem(2, 0, 0, true, "Capability"));
        list.add(new TreeItem(3, 0, 0, true, "MaxReadLength"));
        list.add(new TreeItem(4, 0, 0, true, "MaxWriteLength"));
        list.add(new TreeItem(5, 0, 0, true, "readAccess"));
        list.add(new TreeItem(6, 0, 0, true, "writeAccess"));
        list.add(new TreeItem(7, 0, 1, true, StringUtils.bytesToHexString(mTag.getId())));
        if(mifareClassicClass != null){
            list.add(new TreeItem(8, 2, 1, true, ""+ mifareClassicClass.getTagCapabilityLength() +"bytes"));
            list.add(new TreeItem(9, 3, 1, true, ""+ mifareClassicClass.getTagMaxTransceiveLength() +"bytes"));
            list.add(new TreeItem(10, 4, 1, true, ""+ mifareClassicClass.getTagMaxTransceiveLength() +"bytes"));
            if(mifareClassicClass.getReadAccess() == 0x00){
                list.add(new TreeItem(11, 5, 1, true, "Read free"));
            }else{
                list.add(new TreeItem(11, 5, 1, true, "Read protected"));
            }
            if(mifareClassicClass.getWriteAccess() == 0x00){
                list.add(new TreeItem(12, 6, 1, true, "Write free"));
            }else{
                list.add(new TreeItem(12, 6, 1, true, "Write protected"));
            }
            for(int i=0;i<techList.length;i++){
                list.add(new TreeItem(13 + i, 1, 1, true, techList[i]));
            }
        }

        final TagInfoAdapter adapter = new TagInfoAdapter(list,this);
        adapter.setOnInnerItemClickListener(new TreeAdapter.OnInnerItemClickListener<TreeItem>() {
            @Override
            public void onClick(TreeItem node) {
                Log.i(activityTAG , "click: " + node.name);
            }
        });
        adapter.setOnInnerItemLongClickListener(new TreeAdapter.OnInnerItemLongClickListener<TreeItem>() {
            @Override
            public void onLongClick(TreeItem node) {
                Log.i(activityTAG , "long click: " + node.name);
            }
        });
        taginfo_list_view.setAdapter(adapter);
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
