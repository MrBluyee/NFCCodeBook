package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mrbluyee.nfccodebook.R;

public class ChangePasswdActivity extends Activity {
    private EditText editText_Check_old_Passwd;
    private EditText editText_Create_new_Passwd;
    private EditText editText_Check_new_Passwd;
    private ImageButton button_Change_Passwd;
    private String oldPasswd;
    private String newPasswd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_passwd_view);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
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

            }
        });
    }
}
