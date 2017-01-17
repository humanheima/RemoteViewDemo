package com.hm.remoteviewdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemoteActivity extends AppCompatActivity {

    private static final String TAG = "RemoteActivity";
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.remote_views_content)
    LinearLayout remoteViewsContent;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = intent.getParcelableExtra(MainActivity.EXTRA_REMOTE_VIEWS);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    private void updateUI(RemoteViews remoteViews) {
        View view = remoteViews.apply(this, remoteViewsContent);
        remoteViewsContent.addView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        ButterKnife.bind(this);
        IntentFilter filter = new IntentFilter(MainActivity.ACTION_REMOTE);
        registerReceiver(receiver, filter);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemoteActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
