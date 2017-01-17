package com.hm.remoteviewdemo.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hm.remoteviewdemo.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "com.hm.remoteviewdemo.appwidget.click";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG, "onReceive: action = " + intent.getAction());
        //判断如果是自己的action，做自己的事情
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context, "MyAppWidgetProvider onReceive ", Toast.LENGTH_SHORT).show();
            /*Intent intent1 = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent1);*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon1);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.imageView1, rotateBitmap(context, bitmap, degree));
                        Intent clickIntent = new Intent();
                        clickIntent.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context
                                , 0, clickIntent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
                        appWidgetManager.updateAppWidget(
                                new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }

    private Bitmap rotateBitmap(Context context, Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap,
                0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap1;
    }

    /**
     * 桌面小部件更新
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.e(TAG, "appWidgetId = " + appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        views.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * 每次桌面小部件更新时都调用一次这个方法
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Log.e(TAG, "onUpdate");
    }

    /**
     * 小部件第一次添加到桌面的时候调用，可添加多次，单只在第一次的时候调用
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.e(TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.e(TAG, "onDisabled");
    }

}

