package com.example.DuAnMau_PH63816.common;

import android.content.Intent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomBottomButton;
import com.example.DuAnMau_PH63816.homepage.HomePageScreen;

public final class BottomButtonNavigator {

    public static final int TAB_HOME = 0;
    public static final int TAB_PRODUCT = 1;
    public static final int TAB_NOTIFICATION = 2;
    public static final int TAB_PROFILE = 3;

    private BottomButtonNavigator() {
    }

    public static void bindDefaultButtons(AppCompatActivity activity) {
        bindDefaultButtons(activity, TAB_HOME);
    }

    public static void bindDefaultButtons(AppCompatActivity activity, int selectedTab) {
        configure(activity, R.id.btnHome, activity.getString(R.string.tab_home), selectedTab == TAB_HOME);
        configure(activity, R.id.btnCart, activity.getString(R.string.tab_cart), selectedTab == TAB_PRODUCT);
        configure(activity, R.id.btnNotification, activity.getString(R.string.tab_notification), selectedTab == TAB_NOTIFICATION);
        configure(activity, R.id.btnProfile, activity.getString(R.string.tab_profile), selectedTab == TAB_PROFILE);

        bind(activity, R.id.btnHome, TAB_HOME);
        bind(activity, R.id.btnCart, TAB_PRODUCT);
        bind(activity, R.id.btnNotification, TAB_NOTIFICATION);
        bind(activity, R.id.btnProfile, TAB_PROFILE);
    }

    private static void bind(AppCompatActivity activity, @IdRes int viewId, int tabIndex) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            return;
        }
        view.setOnClickListener(v -> openTab(activity, tabIndex));
    }

    public static void openTab(AppCompatActivity activity, int tabIndex) {
        Intent intent = new Intent(activity, HomePageScreen.class);
        intent.putExtra("extra_initial_tab", tabIndex);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void configure(AppCompatActivity activity, @IdRes int viewId, CharSequence label, boolean selected) {
        View view = activity.findViewById(viewId);
        if (view instanceof CustomBottomButton) {
            CustomBottomButton button = (CustomBottomButton) view;
            button.setLabel(label);
            button.setSelectedState(selected);
        }
    }
}
