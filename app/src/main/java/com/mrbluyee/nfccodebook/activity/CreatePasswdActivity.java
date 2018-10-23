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

public class CreatePasswdActivity extends Activity {
    private EditText editText_Create_Passwd;
    private EditText editText_Check_Passwd;
    private ImageButton button_Create_Passwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_passwd_view);
        initView();
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(!packagename.equals(getPackageName())) {
            finish();
        }
    }

    private void initView(){
        editText_Create_Passwd = (EditText)findViewById(R.id.editText_Create_Passwd);
        editText_Check_Passwd = (EditText)findViewById(R.id.editText_Check_Passwd);
        button_Create_Passwd = (ImageButton)findViewById(R.id.button_Create_Passwd);
        button_Create_Passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd1 = editText_Create_Passwd.getText().toString();
                String passwd2 = editText_Check_Passwd.getText().toString();
                if(!passwd1.equals("")){
                    if(passwd1.equals(passwd2)){
                        Intent intent = new Intent(CreatePasswdActivity.this,ListViewActivity.class);
                        intent.setPackage(getPackageName());
                        Bundle bundle = new Bundle();
                        bundle.putString("newkey",passwd1);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(CreatePasswdActivity.this,"Different passwords!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreatePasswdActivity.this,"Please enter a new password!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
