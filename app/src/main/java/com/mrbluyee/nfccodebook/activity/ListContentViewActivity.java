package com.mrbluyee.nfccodebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.CodeBook;
import com.mrbluyee.nfccodebook.utils.CodeRecord;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class ListContentViewActivity extends AppCompatActivity implements View.OnClickListener {
    private CodeBook codeBook;
    private String recordName;
    private CodeRecord codeRecord;
    private boolean dataChanged = false;

    private EditText editText_Record;
    private EditText editText_Account;
    private EditText editText_Password;                
    private EditText editText_Remark;

    private Button button_Clipboard_Record;
    private Button button_Notification_Record;
    private Button button_Edit_Record;
    private Button button_Clipboard_Account;
    private Button button_Notification_Account;
    private Button button_Edit_Account;
    private Button button_Clipboard_Password;
    private Button button_Notification_Password;
    private Button button_Edit_Password;
    private Button button_Clipboard_Remark;
    private Button button_Notification_Remark;
    private Button button_Edit_Remark;
    private Button button_Record_Delete;
    private Button button_Record_Save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_content_view);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
            this.codeBook = new CodeBook(serializableHashMap.getMap());
            this.recordName = bundle.getString("record");
            this.codeRecord = this.codeBook.book.get(this.recordName);
            editText_Record.setText(this.recordName);
            editText_Account.setText(this.codeRecord.account);
            editText_Password.setText(this.codeRecord.password);
            editText_Remark.setText(this.codeRecord.remark);
            setEditTextEditable(editText_Record,false);
            setEditTextEditable(editText_Account,false);
            setEditTextEditable(editText_Password,false);
            setEditTextEditable(editText_Remark,false);
        }
    }

    private void setEditTextEditable(EditText editText, boolean value) {
        if (value) {
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        } else {
            editText.setFocusableInTouchMode(false);
            editText.clearFocus();
        }
    }

    private void initView(){
        editText_Record = (EditText)findViewById(R.id.editText_Record);
        editText_Account = (EditText)findViewById(R.id.editText_Account);
        editText_Password = (EditText)findViewById(R.id.editText_Password);
        editText_Remark = (EditText)findViewById(R.id.editText_Remark);
        button_Clipboard_Record = (Button)findViewById(R.id.button_Clipboard_Record);
        button_Notification_Record = (Button)findViewById(R.id.button_Notification_Record);
        button_Edit_Record = (Button)findViewById(R.id.button_Edit_Record);
        button_Clipboard_Account = (Button)findViewById(R.id.button_Clipboard_Account);
        button_Notification_Account = (Button)findViewById(R.id.button_Notification_Account);
        button_Edit_Account = (Button)findViewById(R.id.button_Edit_Account);
        button_Clipboard_Password = (Button)findViewById(R.id.button_Clipboard_Password);
        button_Notification_Password = (Button)findViewById(R.id.button_Notification_Password);
        button_Edit_Password = (Button)findViewById(R.id.button_Edit_Password);
        button_Clipboard_Remark = (Button)findViewById(R.id.button_Clipboard_Remark);
        button_Notification_Remark = (Button)findViewById(R.id.button_Notification_Remark);
        button_Edit_Remark = (Button)findViewById(R.id.button_Edit_Remark);
        button_Record_Delete = (Button)findViewById(R.id.button_Record_Delete);
        button_Record_Save = (Button)findViewById(R.id.button_Record_Save);
        button_Clipboard_Record.setOnClickListener(this);
        button_Notification_Record.setOnClickListener(this);
        button_Edit_Record.setOnClickListener(this);
        button_Clipboard_Account.setOnClickListener(this);
        button_Notification_Account.setOnClickListener(this);
        button_Edit_Account.setOnClickListener(this);
        button_Clipboard_Password.setOnClickListener(this);
        button_Notification_Password.setOnClickListener(this);
        button_Edit_Password.setOnClickListener(this);
        button_Clipboard_Remark.setOnClickListener(this);
        button_Notification_Remark.setOnClickListener(this);
        button_Edit_Remark.setOnClickListener(this);
        button_Record_Delete.setOnClickListener(this);
        button_Record_Save.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Clipboard_Record:{

                break;
            }
            case R.id.button_Notification_Record:{

                break;
            }
            case R.id.button_Edit_Record:{
                setEditTextEditable(editText_Record, true);
                break;
            }
            case R.id.button_Clipboard_Account:{

                break;
            }
            case R.id.button_Notification_Account:{

                break;
            }
            case R.id.button_Edit_Account:{
                setEditTextEditable(editText_Account, true);
                break;
            }
            case R.id.button_Clipboard_Password:{

                break;
            }
            case R.id.button_Notification_Password:{

                break;
            }
            case R.id.button_Edit_Password:{
                setEditTextEditable(editText_Password, true);
                break;
            }
            case R.id.button_Clipboard_Remark:{

                break;
            }
            case R.id.button_Notification_Remark:{

                break;
            }
            case R.id.button_Edit_Remark:{
                setEditTextEditable(editText_Remark, true);
                break;
            }
            case R.id.button_Record_Delete:{

                break;
            }
            case R.id.button_Record_Save:{

                break;
            }
            default:
                break;
        }
    }
}
