package com.mrbluyee.nfccodebook.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.mrbluyee.nfccodebook.R;

public class NotificationUtils {
    private Context context;
    private NotificationManager manager;
    private final String CHANNEL_ID = "channel_id_1";
    private final String CHANNEL_NAME = "channel_name_1";

    public NotificationUtils(Context context){
        this.context = context;
        manager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void notifiShowText(int notifyId,String title,String text_str){
        NotificationCompat.BigTextStyle bigTextStyle=new NotificationCompat.BigTextStyle().bigText(text_str);
        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder =  new NotificationCompat.Builder(context,CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(context);
        }
        builder.setSmallIcon(R.mipmap.notificationpng)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
        Notification notification = builder.build();
        manager.notify(notifyId, notification);
    }

    public void  notifiHide(int notifyId){
        manager.cancel(notifyId);
    }
}
