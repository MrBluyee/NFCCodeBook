package com.mrbluyee.nfccodebook.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class TagInfoActivity extends AppCompatActivity {
    private Tag mTag;
    private ListView taginfo_list_view;
    private Button button_Create_CodeBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_info_view);
        Intent intent = getIntent();
        mTag = intent.getParcelableExtra("mtag");
        initView();
    }

    private void initView(){
        taginfo_list_view = (ListView)findViewById(R.id.taginfo_list_view);
        button_Create_CodeBook = (Button)findViewById(R.id.button_Create_CodeBook);
        button_Create_CodeBook.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(TagInfoActivity.this, ListViewActivity.class);
                  startActivity(intent);
              }
            }
        );
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
