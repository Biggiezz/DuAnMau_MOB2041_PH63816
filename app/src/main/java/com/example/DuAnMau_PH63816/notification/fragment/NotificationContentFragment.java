package com.example.DuAnMau_PH63816.notification.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
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
import com.example.DuAnMau_PH63816.notification.config.ConfigNotification;
import com.example.DuAnMau_PH63816.product.ProductScreen;
import com.google.android.material.button.MaterialButton;


public class NotificationContentFragment extends Fragment {

    private static final int CHECKOUT_NOTIFICATION_ID = 1001;
    private static final int REQUEST_POST_NOTIFICATIONS = 2001;

    private TextView txtNotificationTitle;
    private TextView txtNotificationMessage;
    private MaterialButton btnShopping;
    private ImageView imgSuccess;


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
        btnShopping = view.findViewById(R.id.btnShopping);
        imgSuccess = view.findViewById(R.id.imgSuccess);
        showDefaultState();
        btnShopping.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProductScreen.class);
            startActivity(intent);
        });
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

//        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(requireContext(), ConfigNotification.CHANNEL_ID)

                        /// icon hiển thị trên status bar
                        .setSmallIcon(R.drawable.ic_notification)

                        /// tiêu đề của notification
                        .setContentTitle("JP Mart")

                        /// nội dung cả notification
                        .setContentText("Thanh toán thành công")

                        /// truyền 1 hình ảnh vào notification
//                        .setStyle(new NotificationCompat.BigPictureStyle()
//                                .bigLargeIcon(logo)
//                                .bigLargeIcon((Bitmap) null)
//                        )

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
            txtNotificationMessage.setText(R.string.dont_have_any_products);
        }
        if (btnShopping != null) {
            btnShopping.setVisibility(View.GONE);
        }
        if (imgSuccess != null) {
            imgSuccess.setVisibility(View.GONE);
        }
    }

    private void showCheckoutState() {
        if (txtNotificationTitle != null) {
            txtNotificationTitle.setText(R.string.payment_successful);
        }
        if (txtNotificationMessage != null) {
            txtNotificationMessage.setText(R.string.your_order_has_been_confirmed);
        }
        if (btnShopping != null) {
            btnShopping.setVisibility(View.VISIBLE);
        }
        if (imgSuccess != null) {
            imgSuccess.setVisibility(View.VISIBLE);
        }
    }

    private void showPermissionState() {
        if (txtNotificationMessage != null) {
            txtNotificationMessage.setText(R.string.grant_notification_permission);
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
