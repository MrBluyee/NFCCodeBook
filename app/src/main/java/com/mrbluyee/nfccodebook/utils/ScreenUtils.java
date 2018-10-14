package com.mrbluyee.nfccodebook.utils;

import android.content.Context;

public class ScreenUtils {
    Context context;

    public ScreenUtils(Context context){
        this.context = context;
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
