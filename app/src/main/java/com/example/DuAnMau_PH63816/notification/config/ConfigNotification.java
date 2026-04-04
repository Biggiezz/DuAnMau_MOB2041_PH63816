package com.example.DuAnMau_PH63816.notification.config;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.DuAnMau_PH63816.R;

public class ConfigNotification extends Application {

    public static final String CHANNEL_ID = "JP Mart";

    @Override
    public void onCreate() {
        super.onCreate();
        config();
    }

    private void config() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(uri, audioAttributes);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
