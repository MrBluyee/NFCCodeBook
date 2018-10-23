package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class ChangePasswdActivity extends Activity {
    private EditText editText_Check_old_Passwd;
    private EditText editText_Create_new_Passwd;
    private EditText editText_Check_new_Passwd;
    private ImageButton button_Change_Passwd;
    private String oldPasswd = null;
    private String newPasswd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_passwd_view);
        initView();
        Intent intent = getIntent();
        intent.getComponent().getPackageName().toString();

        Bundle bundle = intent.getBundleExtra("bundle");
        oldPasswd = bundle.getString("key");
    }
    private void initView(){
        editText_Check_old_Passwd = (EditText)findViewById(R.id.editText_Check_old_Passwd);
        editText_Create_new_Passwd = (EditText)findViewById(R.id.editText_Create_new_Passwd);
        editText_Check_new_Passwd = (EditText)findViewById(R.id.editText_Check_new_Passwd);
        button_Change_Passwd = (ImageButton)findViewById(R.id.button_Change_Passwd);
        button_Change_Passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_passwd_input = editText_Check_old_Passwd.getText().toString();
                if(old_passwd_input.equals(oldPasswd)){
                    String new_passwd_input =  editText_Create_new_Passwd.getText().toString();
                    if(!new_passwd_input.equals("")){
                        String new_passwd_check = editText_Check_new_Passwd.getText().toString();
                        if(new_passwd_input.equals(new_passwd_check)){
                            newPasswd = new_passwd_input;
                            Toast.makeText(ChangePasswdActivity.this,"New password saved",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChangePasswdActivity.this,"Different new passwords",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ChangePasswdActivity.this,"Please enter a new password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePasswdActivity.this,"Incorrect old password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setPackage(getPackageName());
        if(newPasswd != null){
            Bundle bundle=new Bundle();
            bundle.putString("newkey",newPasswd);
            intent.putExtra("bundle",bundle);
            setResult(StatusCode.DATAUPDATED, intent);//数据更新
        }else {
            setResult(StatusCode.DATANOTUPDATED, intent);//数据未更新
        }
        super.onBackPressed();
    }
}
