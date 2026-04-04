package com.example.DuAnMau_PH63816.notification.config;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.DuAnMau_PH63816.R;


public class NotificationContentFragment extends Fragment {

    private static final int CHECKOUT_NOTIFICATION_ID = 1001;
    private static final int REQUEST_POST_NOTIFICATIONS = 2001;

    private TextView txtNotificationTitle;
    private TextView txtNotificationMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_content, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtNotificationTitle = view.findViewById(R.id.txtNotificationTitle);
        txtNotificationMessage = view.findViewById(R.id.txtNotificationMessage);

        showDefaultState();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean fromCheckout = requireActivity()
                .getIntent()
                .getBooleanExtra("from_checkout", false);
        if (!fromCheckout) {
            showDefaultState();
            return;
        }
        showCheckoutState();
        maybeShowCheckoutNotification();
    }

    private void maybeShowCheckoutNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_POST_NOTIFICATIONS
            );
            showPermissionState();
            return;
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(requireContext(), ConfigNotification.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("JP Mart")
                        .setContentText("Thanh toán thành công")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(requireContext())
                .notify(CHECKOUT_NOTIFICATION_ID, builder.build());
        requireActivity().getIntent().removeExtra("from_checkout");
    }

    private void showDefaultState() {
        if (txtNotificationTitle != null) {
            txtNotificationTitle.setText(R.string.notification);
        }
        if (txtNotificationMessage != null) {
            txtNotificationMessage.setText(R.string.placeholder_coming_soon);
        }
    }

    private void showCheckoutState() {
        if (txtNotificationTitle != null) {
            txtNotificationTitle.setText("Thanh toán thành công");
        }
        if (txtNotificationMessage != null) {
            txtNotificationMessage.setText("Đơn hàng của bạn đã được ghi nhận.");
        }
    }

    private void showPermissionState() {
        if (txtNotificationMessage != null) {
            txtNotificationMessage.setText("Hãy cấp quyền thông báo để hệ thống gửi xác nhận thanh toán.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_POST_NOTIFICATIONS) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            maybeShowCheckoutNotification();
            return;
        }
        showPermissionState();
    }
}
