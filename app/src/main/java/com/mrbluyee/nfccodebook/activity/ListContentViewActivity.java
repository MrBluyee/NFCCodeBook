package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.bean.CodeBook;
import com.mrbluyee.nfccodebook.bean.CodeRecord;
import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.ClipboardUtils;
import com.mrbluyee.nfccodebook.utils.NotificationUtils;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

import java.util.HashMap;

public class ListContentViewActivity extends Activity implements View.OnClickListener {
    private CodeBook codeBook = null;
    private String recordName = null;
    private CodeRecord codeRecord = null;
    private boolean dataChanged = false;

    private EditText editText_Record;
    private EditText editText_Account;
    private EditText editText_Password;
    private EditText editText_Remark;
    private ClipboardUtils clipboardUtils;
    private NotificationUtils notificationUtils;

    private ImageButton button_View_Record;
    private ImageButton button_Clipboard_Record;
    private ImageButton button_Notification_Record;
    private ImageButton button_Edit_Record;
    private ImageButton button_View_Account;
    private ImageButton button_Clipboard_Account;
    private ImageButton button_Notification_Account;
    private ImageButton button_Edit_Account;
    private ImageButton button_View_Password;
    private ImageButton button_Clipboard_Password;
    private ImageButton button_Notification_Password;
    private ImageButton button_Edit_Password;
    private ImageButton button_View_Remark;
    private ImageButton button_Clipboard_Remark;
    private ImageButton button_Notification_Remark;
    private ImageButton button_Edit_Remark;
    private ImageButton button_Record_ShowAll;
    private ImageButton button_Record_Random;
    private ImageButton button_Record_Delete;
    private ImageButton button_Record_Save;

    private boolean button_View_Record_status = false;
    private boolean button_Clipboard_Record_status = false;
    private boolean button_Notification_Record_status = false;
    private boolean button_Edit_Record_status = false;
    private boolean button_View_Account_status = false;
    private boolean button_Clipboard_Account_status = false;
    private boolean button_Notification_Account_status = false;
    private boolean button_Edit_Account_status = false;
    private boolean button_View_Password_status = false;
    private boolean button_Clipboard_Password_status = false;
    private boolean button_Notification_Password_status = false;
    private boolean button_Edit_Password_status = false;
    private boolean button_View_Remark_status = false;
    private boolean button_Clipboard_Remark_status = false;
    private boolean button_Notification_Remark_status = false;
    private boolean button_Edit_Remark_status = false;
    private boolean button_Record_ShowAll_status = false;

    private String str_Record_Temp;
    private String str_Account_Temp;
    private String str_Password_Temp;
    private String str_Remark_Temp;

    private final String hide_password = "**********";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_content_view);
        clipboardUtils = new ClipboardUtils(this);
        notificationUtils = new NotificationUtils(this);
        initView();
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(packagename.equals(getPackageName())) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if(bundle != null) {
                SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("map");
                recordName = bundle.getString("record");
                if(serializableHashMap != null) {
                    codeBook = new CodeBook(serializableHashMap.getMap());
                }
            }
        }else{
            finish();
        }
        if(recordName != null) {
            codeRecord = codeBook.book.get(recordName);
            editText_Record.setText(recordName);
            editText_Account.setText(codeRecord.account);
            editText_Password.setText(codeRecord.password);
            editText_Remark.setText(codeRecord.remark);
            setEditTextEditable(editText_Record,false);
            setEditTextEditable(editText_Account,false);
            setEditTextEditable(editText_Password,false);
            setEditTextEditable(editText_Remark,false);
        }else{
            button_View_Record_status = true;
            button_View_Account_status = true;
            button_View_Password_status = true;
            button_View_Remark_status = true;
            button_Record_ShowAll_status = true;
            button_Edit_Record_status = true;
            button_Edit_Account_status = true;
            button_Edit_Password_status = true;
            button_Edit_Remark_status = true;
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
        button_View_Record = (ImageButton)findViewById(R.id.button_View_Record);
        button_Clipboard_Record = (ImageButton)findViewById(R.id.button_Clipboard_Record);
        button_Notification_Record = (ImageButton)findViewById(R.id.button_Notification_Record);
        button_Edit_Record = (ImageButton)findViewById(R.id.button_Edit_Record);
        button_View_Account = (ImageButton)findViewById(R.id.button_View_Account);
        button_Clipboard_Account = (ImageButton)findViewById(R.id.button_Clipboard_Account);
        button_Notification_Account = (ImageButton)findViewById(R.id.button_Notification_Account);
        button_Edit_Account = (ImageButton)findViewById(R.id.button_Edit_Account);
        button_View_Password = (ImageButton)findViewById(R.id.button_View_Password);
        button_Clipboard_Password = (ImageButton)findViewById(R.id.button_Clipboard_Password);
        button_Notification_Password = (ImageButton)findViewById(R.id.button_Notification_Password);
        button_Edit_Password = (ImageButton)findViewById(R.id.button_Edit_Password);
        button_View_Remark = (ImageButton)findViewById(R.id.button_View_Remark);
        button_Clipboard_Remark = (ImageButton)findViewById(R.id.button_Clipboard_Remark);
        button_Notification_Remark = (ImageButton)findViewById(R.id.button_Notification_Remark);
        button_Edit_Remark = (ImageButton)findViewById(R.id.button_Edit_Remark);
        button_Record_ShowAll = (ImageButton)findViewById(R.id.button_Record_ShowAll);
        button_Record_Random = (ImageButton)findViewById(R.id.button_Record_Random);
        button_Record_Delete = (ImageButton)findViewById(R.id.button_Record_Delete);
        button_Record_Save = (ImageButton)findViewById(R.id.button_Record_Save);
        button_View_Record.setOnClickListener(this);
        button_Clipboard_Record.setOnClickListener(this);
        button_Notification_Record.setOnClickListener(this);
        button_Edit_Record.setOnClickListener(this);
        button_View_Account.setOnClickListener(this);
        button_Clipboard_Account.setOnClickListener(this);
        button_Notification_Account.setOnClickListener(this);
        button_Edit_Account.setOnClickListener(this);
        button_View_Password.setOnClickListener(this);
        button_Clipboard_Password.setOnClickListener(this);
        button_Notification_Password.setOnClickListener(this);
        button_Edit_Password.setOnClickListener(this);
        button_View_Remark.setOnClickListener(this);
        button_Clipboard_Remark.setOnClickListener(this);
        button_Notification_Remark.setOnClickListener(this);
        button_Edit_Remark.setOnClickListener(this);
        button_Record_ShowAll.setOnClickListener(this);
        button_Record_Random.setOnClickListener(this);
        button_Record_Delete.setOnClickListener(this);
        button_Record_Save.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setPackage(getPackageName());
        if(dataChanged){
            SerializableHashMap myMap = new SerializableHashMap();
            myMap.setMap(codeBook.book);
            Bundle bundle=new Bundle();
            bundle.putSerializable("map",myMap);
            intent.putExtra("bundle",bundle);
            setResult(StatusCode.DATAUPDATED, intent);//数据更新
        }else {
            setResult(StatusCode.DATANOTUPDATED, intent);//数据未更新
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
        String editText_Temp1 = editText_Record.getText().toString();
        String editText_Temp2 = editText_Account.getText().toString();
        String editText_Temp3 = editText_Password.getText().toString();
        String editText_Temp4 = editText_Remark.getText().toString();
        if(!editText_Temp1.equals(hide_password)) {
            if(editText_Temp1 != null) {
                str_Record_Temp = editText_Temp1;
            }
        }
        if(!editText_Temp2.equals(hide_password)) {
            if(editText_Temp2 != null) {
                str_Account_Temp = editText_Temp2;
            }
        }
        if(!editText_Temp3.equals(hide_password)) {
            if(editText_Temp3 != null) {
                str_Password_Temp = editText_Temp3;
            }
        }
        if(!editText_Temp4.equals(hide_password)) {
            if(editText_Temp4 != null) {
                str_Remark_Temp = editText_Temp4;
            }
        }
        switch (v.getId()) {
            case R.id.button_View_Record:{
                button_View_Record_status = !button_View_Record_status;
                if(button_View_Record_status){
                    editText_Record.setText(str_Record_Temp);
                }else{
                    editText_Record.setText(hide_password);
                }
                break;
            }
            case R.id.button_Clipboard_Record:{
                button_Clipboard_Record_status = !button_Clipboard_Record_status;
                if(button_Clipboard_Record_status){
                    clipboardUtils.putTextIntoClip(str_Record_Temp);
                    Toast.makeText(this,"Copy to clipboard", Toast.LENGTH_SHORT).show();
                }else{
                    clipboardUtils.clearClip();
                    Toast.makeText(this,"Clear clipboard", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_Notification_Record:{
                button_Notification_Record_status = !button_Notification_Record_status;
                if(button_Notification_Record_status){
                    notificationUtils.notifiShowText(1,"Record name",str_Record_Temp);
                }else{
                    notificationUtils.notifiHide(1);
                }
                break;
            }
            case R.id.button_Edit_Record:{
                button_Edit_Record_status = !button_Edit_Record_status;
                if(button_Edit_Record_status) {
                    setEditTextEditable(editText_Record, true);
                }else{
                    setEditTextEditable(editText_Record, false);
                }
                break;
            }
            case R.id.button_View_Account:{
                button_View_Account_status = !button_View_Account_status;
                if(button_View_Account_status){
                    editText_Account.setText(str_Account_Temp);
                }else{
                    editText_Account.setText(hide_password);
                }
                break;
            }
            case R.id.button_Clipboard_Account:{
                button_Clipboard_Account_status = !button_Clipboard_Account_status;
                if(button_Clipboard_Account_status){
                    clipboardUtils.putTextIntoClip(str_Account_Temp);
                    Toast.makeText(this,"Copy to clipboard", Toast.LENGTH_SHORT).show();
                }else{
                    clipboardUtils.clearClip();
                    Toast.makeText(this,"Clear clipboard", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_Notification_Account:{
                button_Notification_Account_status = !button_Notification_Account_status;
                if(button_Notification_Account_status){
                    notificationUtils.notifiShowText(2,"Account",str_Account_Temp);
                }else{
                    notificationUtils.notifiHide(2);
                }
                break;
            }
            case R.id.button_Edit_Account:{
                button_Edit_Account_status = !button_Edit_Account_status;
                if(button_Edit_Account_status) {
                    setEditTextEditable(editText_Account, true);
                }else{
                    setEditTextEditable(editText_Account, false);
                }
                break;
            }
            case R.id.button_View_Password:{
                button_View_Password_status = !button_View_Password_status;
                if(button_View_Password_status){
                    editText_Password.setText(str_Password_Temp);
                }else{
                    editText_Password.setText(hide_password);
                }
                break;
            }
            case R.id.button_Clipboard_Password:{
                button_Clipboard_Password_status = !button_Clipboard_Password_status;
                if(button_Clipboard_Password_status){
                    clipboardUtils.putTextIntoClip(str_Password_Temp);
                    Toast.makeText(this,"Copy to clipboard", Toast.LENGTH_SHORT).show();
                }else{
                    clipboardUtils.clearClip();
                    Toast.makeText(this,"Clear clipboard", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_Notification_Password:{
                button_Notification_Password_status = !button_Notification_Password_status;
                if(button_Notification_Password_status){
                    notificationUtils.notifiShowText(3,"Password",str_Password_Temp);
                }else{
                    notificationUtils.notifiHide(3);
                }
                break;
            }
            case R.id.button_Edit_Password:{
                button_Edit_Password_status = !button_Edit_Password_status;
                if(button_Edit_Password_status) {
                    setEditTextEditable(editText_Password, true);
                }else{
                    setEditTextEditable(editText_Password, false);
                }
                break;
            }
            case R.id.button_View_Remark:{
                button_View_Remark_status = !button_View_Remark_status;
                if(button_View_Remark_status){
                    editText_Remark.setText(str_Remark_Temp);
                }else{
                    editText_Remark.setText(hide_password);
                }
                break;
            }
            case R.id.button_Clipboard_Remark:{
                button_Clipboard_Remark_status = !button_Clipboard_Remark_status;
                if(button_Clipboard_Remark_status){
                    clipboardUtils.putTextIntoClip(str_Remark_Temp);
                    Toast.makeText(this,"Copy to clipboard", Toast.LENGTH_SHORT).show();
                }else{
                    clipboardUtils.clearClip();
                    Toast.makeText(this,"Clear clipboard", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_Notification_Remark:{
                button_Notification_Remark_status = !button_Notification_Remark_status;
                if(button_Notification_Remark_status){
                    notificationUtils.notifiShowText(4,"Remark",str_Remark_Temp);
                }else{
                    notificationUtils.notifiHide(4);
                }
                break;
            }
            case R.id.button_Edit_Remark:{
                button_Edit_Remark_status = !button_Edit_Remark_status;
                if(button_Edit_Remark_status) {
                    setEditTextEditable(editText_Remark, true);
                }else{
                    setEditTextEditable(editText_Remark, false);
                }
                break;
            }
            case R.id.button_Record_ShowAll:{
                button_Record_ShowAll_status = !button_Record_ShowAll_status;
                if(button_Record_ShowAll_status) {
                    editText_Record.setText(str_Record_Temp);
                    editText_Account.setText(str_Account_Temp);
                    editText_Password.setText(str_Password_Temp);
                    editText_Remark.setText(str_Remark_Temp);
                }else{
                    editText_Record.setText(hide_password);
                    editText_Account.setText(hide_password);
                    editText_Password.setText(hide_password);
                    editText_Remark.setText(hide_password);
                }
                break;
            }
            case R.id.button_Record_Random:{
                Intent intent = new Intent(ListContentViewActivity.this,RandomPasswdActivity.class);
                intent.setPackage(getPackageName());
                startActivity(intent);
                break;
            }
            case R.id.button_Record_Delete:{
                if(recordName != null){
                    if(codeBook.book.get(recordName) != null) {
                        codeBook.book.remove(recordName);
                        Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                        dataChanged = true;
                    }
                }else{
                    if(codeBook.book.get(str_Record_Temp) != null){
                        codeBook.book.remove(str_Record_Temp);
                        Toast.makeText(this,"Record deleted", Toast.LENGTH_SHORT).show();
                        dataChanged = true;
                    }
                }
                break;
            }
            case R.id.button_Record_Save:{
                if(!str_Record_Temp.equals("")) {
                    if (recordName != null) { //更新
                        if (codeBook.book.get(recordName) != null) {
                            codeBook.book.remove(recordName);
                            Toast.makeText(this, "Record changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (codeBook.book.get(str_Record_Temp) != null) {
                        codeBook.book.remove(str_Record_Temp);
                    }
                    Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show();
                    codeRecord = new CodeRecord();
                    codeRecord.account = str_Account_Temp;
                    codeRecord.password = str_Password_Temp;
                    codeRecord.remark = str_Remark_Temp;
                    codeBook.book.put(str_Record_Temp, codeRecord);
                    dataChanged = true;
                }else {
                    Toast.makeText(this, "Empty content", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }
}
