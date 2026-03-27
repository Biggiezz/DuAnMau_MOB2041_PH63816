package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.common.BottomTabPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductScreen extends AppCompatActivity implements BottomTabHost {

    private ViewPager2 viewPagerProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private void initUi() {
        Toolbar toolbarProductScreen = findViewById(R.id.toolbarProductScreen);
        ImageView imgAddProduct = findViewById(R.id.imgAddProduct);
        TabLayout tabLayout = findViewById(R.id.tabLayoutBottom);
        viewPagerProduct = findViewById(R.id.viewPagerProduct);

        if (toolbarProductScreen != null) {
            setSupportActionBar(toolbarProductScreen);
            toolbarProductScreen.setNavigationOnClickListener(v -> finish());
        }

        viewPagerProduct.setAdapter(new BottomTabPagerAdapter(this));
        viewPagerProduct.setCurrentItem(1, false);
        toolbarProductScreen.setTitle(getToolbarTitleRes(1));
        new TabLayoutMediator(tabLayout, viewPagerProduct, (tab, position) -> {
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
        viewPagerProduct.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                toolbarProductScreen.setTitle(getToolbarTitleRes(position));
            }
        });

        imgAddProduct.setOnClickListener(v -> startActivity(new Intent(ProductScreen.this, AddProductScreen.class)));
    }

    private int getToolbarTitleRes(int position) {
        if (position == 0) {
            return R.string.home_toolbar_title;
        }
        if (position == 1) {
            return R.string.title_product;
        }
        if (position == 2) {
            return R.string.notification;
        }
        return R.string.setting;
    }

    @Override
    public void openBottomTab(int position) {
        if (viewPagerProduct != null) {
            viewPagerProduct.setCurrentItem(position, true);
        }
    }
}
