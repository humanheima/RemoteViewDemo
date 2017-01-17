package com.hm.remoteviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.widget.RemoteViews;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_REMOTE = "com.hm.remoteviewdemo.action_REMOTE";
    public static final String EXTRA_REMOTE_VIEWS = "extra_remoteViews";
    @BindView(R.id.btn_send_notification)
    Button btnSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void useNotification() {

        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification noti = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("通知title")
                .setContentText("通知内容")
                .setTicker("hello world")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, noti);

    }

    /**
     * 自定义通知
     */
    public void useCustomerNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg, "使用RemoteViews 自定义通知");
        remoteViews.setImageViewResource(R.id.img_icon, R.drawable.icon1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.text_open_notification_activity, pendingIntent);
        Notification noti = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteViews)
                .setWhen(System.currentTimeMillis())
                .build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, noti);
    }

    @OnClick(R.id.btn_send_notification)
    public void onClick() {
        // useNotification();
        useCustomerNotification();
    }

    @OnClick(R.id.btn_update_remote_view)
    public void updateRemoteView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_simulated_notification);
        remoteViews.setTextViewText(R.id.msg, "msg from process:" + Process.myPid());
        remoteViews.setImageViewResource(R.id.img_icon, R.drawable.icon1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationActivity.class)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.item_holder, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.open_activity_notification, pendingIntent);

        Intent intent = new Intent(ACTION_REMOTE);
        intent.putExtra(EXTRA_REMOTE_VIEWS, remoteViews);
        sendBroadcast(intent);
    }

}
