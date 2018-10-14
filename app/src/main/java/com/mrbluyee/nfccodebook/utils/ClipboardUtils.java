package com.mrbluyee.nfccodebook.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {
    private Context context;
    private ClipboardManager clipboardManager;

    public ClipboardUtils(Context context){
        this.context = context;
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void putTextIntoClip(String str){
        ClipData clipData = ClipData.newPlainText("codeBookText", str);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void clearClip() {
        clipboardManager.setPrimaryClip(null);
    }
}
