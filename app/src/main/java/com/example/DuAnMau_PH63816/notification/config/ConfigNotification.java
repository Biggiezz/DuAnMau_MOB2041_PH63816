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

            /// Tên của Notification Channel cần đăng ký
            CharSequence name = getString(R.string.channel_name);

            /// Mô tả của Notification Channel
            String description = getString(R.string.channel_description);

            /// Độ ưu tiên của Notification
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            /// Sử dụng RingtoneManager để lấy Uri của âm thanh notification theo máy
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            /// Tạo thêm một audioAttributes
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            /// Đăng ký NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            /// Set âm thanh cho notification
            channel.setSound(uri, audioAttributes);

            /// Đăng ký channel với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
