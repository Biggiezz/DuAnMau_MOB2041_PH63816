package com.example.DuAnMau_PH63816.category;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.google.android.material.tabs.TabLayout;

public class CategoryManagementScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_management_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private void initUi() {
        Toolbar toolbarCategoryManagementScreen = findViewById(R.id.toolbarCategoryManagementScreen);
        TabLayout tabLayout = findViewById(R.id.tabLayoutBottom);
        ViewPager2 viewPagerCategory = findViewById(R.id.viewPagerCategory);
        if (toolbarCategoryManagementScreen != null) {
            setSupportActionBar(toolbarCategoryManagementScreen);
            toolbarCategoryManagementScreen.setNavigationOnClickListener(v -> finish());
        }
        viewPagerCategory.setAdapter(new CategoryBottomPagerAdapter(this));
        viewPagerCategory.setCurrentItem(0, false);
        if (toolbarCategoryManagementScreen != null) {
            toolbarCategoryManagementScreen.setTitle(getToolbarTitleRes(0));
        }
//        new TabLayoutMediator(tabLayout, viewPagerCategory, (tab, position) -> {
//            if (position == 0) {
//                tab.setText(R.string.home_label_category);
//                tab.setIcon(R.drawable.btn_category);
//            } else if (position == 1) {
//                tab.setText(R.string.tab_cart);
//                tab.setIcon(R.drawable.ic_product);
//            } else if (position == 2) {
//                tab.setText(R.string.tab_notification);
//                tab.setIcon(R.drawable.ic_notification);
//            } else {
//                tab.setText(R.string.tab_profile);
//                tab.setIcon(R.drawable.ic_setting);
//            }
//        }).attach();
        viewPagerCategory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                toolbarCategoryManagementScreen.setTitle(getToolbarTitleRes(position));
            }
        });
    }

    private int getToolbarTitleRes(int position) {
        if (position == 0) {
            return R.string.label_category;
        }
        if (position == 1) {
            return R.string.title_product;
        }
        if (position == 2) {
            return R.string.notification;
        }
        return R.string.setting;
    }
}
