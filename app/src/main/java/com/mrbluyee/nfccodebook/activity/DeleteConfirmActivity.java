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
import com.mrbluyee.nfccodebook.bean.CodeBook;
import com.mrbluyee.nfccodebook.bean.RequestCode;
import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.SerializableHashMap;

public class DeleteConfirmActivity extends Activity {
    private EditText editText_Delete_Check_Passwd;
    private ImageButton button_Delete_Check;
    private String passwd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_confirm_view);
        initView();
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(packagename.equals(getPackageName())) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                passwd = (String) bundle.get("key");
            }
        }else{
            finish();
        }
    }

    private void initView(){
        editText_Delete_Check_Passwd = (EditText)findViewById(R.id.editText_Delete_Check_Passwd);
        button_Delete_Check = (ImageButton)findViewById(R.id.button_Delete_Check);
        button_Delete_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd_check = editText_Delete_Check_Passwd.getText().toString();
                if(!passwd_check.equals("")){
                    if(passwd_check.equals(passwd)){
                        Intent intent = new Intent(DeleteConfirmActivity.this,WriteToTagActivity.class);
                        intent.setPackage(getPackageName());
                        Bundle bundle=new Bundle();
                        bundle.putBoolean("cleartag",true);
                        intent.putExtra("bundle",bundle);
                        startActivityForResult(intent, RequestCode.WRITETOTAG);
                    }else {
                        Toast.makeText(DeleteConfirmActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DeleteConfirmActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String packagename = data.getPackage().toString();
        if(packagename.equals(getPackageName())) {
            if (requestCode == RequestCode.WRITETOTAG) {
                if (resultCode == StatusCode.CLEARTAGSUCCEED) {
                    Intent intent = new Intent(DeleteConfirmActivity.this, MainActivity.class);
                    intent.setPackage(getPackageName());
                    startActivity(intent);
                } else if (resultCode == StatusCode.CLEARTAGFAILED) {
                    finish();
                }
            }
        }else{
            finish();
        }
    }
}
