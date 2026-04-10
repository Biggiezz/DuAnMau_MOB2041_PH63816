package com.example.DuAnMau_PH63816.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.common.BottomTabPagerAdapter;
import com.example.DuAnMau_PH63816.product.CartActivity;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileScreen extends AppCompatActivity implements BottomTabHost {

    private Toolbar toolbarProfileScreen;
    private ViewPager2 viewPagerProfile;
    private TextView txtCartBadgeProfile;
    private View layoutCartActionProfile;
    private TabLayout tabLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        CartManager.initialize(this);

        initUi();

        setSupportActionBar(toolbarProfileScreen);
        toolbarProfileScreen.setNavigationOnClickListener(v -> finish());

        layoutCartActionProfile.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        viewPagerProfile.setAdapter(new BottomTabPagerAdapter(this));
        viewPagerProfile.setCurrentItem(3, false);
        toolbarProfileScreen.setTitle(R.string.setting);

        new TabLayoutMediator(tabLayoutBottom, viewPagerProfile, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.tab_home);
                tab.setIcon(R.drawable.ic_homepage);
            } else if (position == 1) {
                tab.setText(R.string.tab_cart);
                tab.setIcon(R.drawable.ic_product);
            } else if (position == 2) {
                tab.setText(R.string.tab_notification);
                tab.setIcon(R.drawable.ic_notification);
            } else {
                tab.setText(R.string.tab_profile);
                tab.setIcon(R.drawable.ic_setting);
            }
        }).attach();

        viewPagerProfile.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    toolbarProfileScreen.setTitle(R.string.home_toolbar_title);
                } else if (position == 1) {
                    toolbarProfileScreen.setTitle(R.string.title_product);
                } else if (position == 2) {
                    toolbarProfileScreen.setTitle(R.string.notification);
                } else {
                    toolbarProfileScreen.setTitle(R.string.setting);
                }
            }
        });

        updateCartBadge();
    }

    private void initUi() {
        toolbarProfileScreen = findViewById(R.id.toolbarProfileScreen);
        layoutCartActionProfile = findViewById(R.id.layoutCartActionProfile);
        tabLayoutBottom = findViewById(R.id.tabLayoutBottom);
        viewPagerProfile = findViewById(R.id.viewPagerProfile);
        txtCartBadgeProfile = findViewById(R.id.txtCartBadgeProfile);
    }

    @Override
    public void openBottomTab(int position) {
        viewPagerProfile.setCurrentItem(position, true);
    }

    private void updateCartBadge() {
        int totalQuantity = CartManager.getTotalQuantity();
        if (totalQuantity > 0) {
            txtCartBadgeProfile.setVisibility(View.VISIBLE);
            txtCartBadgeProfile.setText(String.valueOf(totalQuantity));
        } else {
            txtCartBadgeProfile.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }
}
