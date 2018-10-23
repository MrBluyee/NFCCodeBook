package com.mrbluyee.nfccodebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbluyee.nfccodebook.R;
import com.mrbluyee.nfccodebook.application.RandomGenerateHandle;
import com.mrbluyee.nfccodebook.application.ReadFromTagHandle;
import com.mrbluyee.nfccodebook.bean.StatusCode;
import com.mrbluyee.nfccodebook.utils.ClipboardUtils;

public class RandomPasswdActivity extends Activity implements View.OnClickListener {
    protected static final String activityTAG = RandomPasswdActivity.class.getName();
    private CheckBox checkBox_Random_Lower;
    private CheckBox checkBox_Random_Number;
    private CheckBox checkBox_Random_Capital;
    private CheckBox checkBox_Random_Special;
    private EditText editText_Random_Length;
    private TextView textView_Generate_Result;
    private ImageButton button_Random_Again;
    private ImageButton button_Random_Copy;
    private MyHandler myHandler;
    private int form_count = 0;
    private int passwd_length = 0;
    private ClipboardUtils clipboardUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_passwd_view);
        clipboardUtils = new ClipboardUtils(this);
        myHandler = new MyHandler();
        initView();
        Intent intent = getIntent();
        String packagename = intent.getPackage().toString();
        if(!packagename.equals(getPackageName())) {
            finish();
        }
    }

    private void initView(){
        checkBox_Random_Lower = (CheckBox)findViewById(R.id.checkBox_Random_Lower);
        checkBox_Random_Number = (CheckBox)findViewById(R.id.checkBox_Random_Number);
        checkBox_Random_Capital = (CheckBox)findViewById(R.id.checkBox_Random_Capital);
        checkBox_Random_Special = (CheckBox)findViewById(R.id.checkBox_Random_Special);
        editText_Random_Length = (EditText)findViewById(R.id.editText_Random_Length);
        textView_Generate_Result = (TextView)findViewById(R.id.textView_Generate_Result);
        button_Random_Again = (ImageButton)findViewById(R.id.button_Random_Again);
        button_Random_Copy = (ImageButton)findViewById(R.id.button_Random_Copy);
        button_Random_Again.setOnClickListener(this);
        button_Random_Copy.setOnClickListener(this);
        textView_Generate_Result.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText_Random_Length.clearFocus();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }
        public MyHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case StatusCode.PASSWDCREATED:
                    textView_Generate_Result.setText((String)msg.obj);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        form_count = 0;
        switch (v.getId()) {
            case R.id.button_Random_Again: {
                if(checkBox_Random_Lower.isChecked()){
                    form_count += 3;
                }
                if(checkBox_Random_Number.isChecked()){
                    form_count += 5;
                }
                if(checkBox_Random_Capital.isChecked()){
                    form_count += 7;
                }
                if(checkBox_Random_Special.isChecked()){
                    form_count += 11;
                }
                if(form_count != 0){
                    String num = editText_Random_Length.getText().toString();
                    if(!num.equals("")) {
                        passwd_length = Integer.parseInt(num);
                        if (passwd_length > 0) {
                            new RandomGenerateHandle(myHandler).new RandomGenerate(form_count, passwd_length).begin();
                        } else {
                            Toast.makeText(RandomPasswdActivity.this, "Length error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RandomPasswdActivity.this, "Please enter generate length", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RandomPasswdActivity.this,"Please select 1 form at least",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_Random_Copy: {
                Toast.makeText(RandomPasswdActivity.this,"Copy to clipboard",Toast.LENGTH_SHORT).show();
                clipboardUtils.putTextIntoClip(textView_Generate_Result.getText().toString());
                break;
            }
        }
    }
}
