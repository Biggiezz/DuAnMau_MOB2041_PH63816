package com.example.DuAnMau_PH63816.homepage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.common.BottomTabPagerAdapter;
import com.example.DuAnMau_PH63816.product.CartAnimationHost;
import com.example.DuAnMau_PH63816.product.CartActivity;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomePageScreen extends AppCompatActivity implements BottomTabHost, CartAnimationHost {


    private ViewPager2 viewPagerHome;
    private int currentTabPosition = 0;
    private Toolbar toolbarHomePageScreen;
    private MenuItem cartMenuItem;
    private View cartActionView;
    private TextView txtCartBadgeMenu;
    private final CartManager.OnCartChangedListener cartChangedListener = this::updateCartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private void initUi() {
        DrawerLayout drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.navigationView);
        toolbarHomePageScreen = findViewById(R.id.toolbarHomePageScreen);
        TabLayout tabLayout = findViewById(R.id.tabLayoutBottom);
        viewPagerHome = findViewById(R.id.viewPagerHome);
        /// toolbar
        setSupportActionBar(toolbarHomePageScreen);
        toolbarHomePageScreen.setNavigationIcon(R.drawable.btn_menu);
        toolbarHomePageScreen.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));
        applyRoleUi(navigationView);

        int initialTab = getIntent().getIntExtra("extra_initial_tab", 0);
        viewPagerHome.setAdapter(new BottomTabPagerAdapter(this));
        viewPagerHome.setCurrentItem(initialTab, false);
        currentTabPosition = initialTab;
        toolbarHomePageScreen.setTitle(getToolbarTitleRes(initialTab));
        new TabLayoutMediator(tabLayout, viewPagerHome, (tab, position) -> {
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
        viewPagerHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTabPosition = position;
                toolbarHomePageScreen.setTitle(getToolbarTitleRes(position));
                invalidateOptionsMenu();
            }
        });

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                if (item.getItemId() == R.id.nav_log_out) {
                    viewPagerHome.setCurrentItem(3, true);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    private void applyRoleUi(NavigationView navigationView) {
        SharedPreferences sharedPreferences = getSharedPreferences("StaffData", MODE_PRIVATE);
        int currentRole = sharedPreferences.getInt("role", 1);
        switch (currentRole) {
            case 0:
                toolbarHomePageScreen.setSubtitle("Quản lý");
                break;
            case 1:
                toolbarHomePageScreen.setSubtitle("Nhân viên");
                break;
            default:
                toolbarHomePageScreen.setSubtitle("Chưa xác định vai trò");
                break;
        }

        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            MenuItem shareItem = menu.findItem(R.id.nav_share);
            if (shareItem != null) {
                shareItem.setVisible(currentRole == 0);
            }
        }
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
        if (viewPagerHome != null) {
            viewPagerHome.setCurrentItem(position, true);
        }
    }

    /// Cách sử dụng Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        cartMenuItem = menu.findItem(R.id.notification);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionItem = menu.findItem(R.id.notification);
        cartMenuItem = actionItem;
        if (actionItem != null) {
            if (currentTabPosition == 1) {
                if (actionItem.getActionView() == null) {
                    actionItem.setActionView(R.layout.view_toolbar_cart_action);
                }
                cartActionView = actionItem.getActionView();
                txtCartBadgeMenu = cartActionView != null
                        ? cartActionView.findViewById(R.id.txtCartBadgeMenu)
                        : null;
                if (cartActionView != null) {
                    cartActionView.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
                }
                actionItem.setTitle(R.string.title_cart);
                updateCartBadge(CartManager.getTotalQuantity());
            } else {
                actionItem.setActionView(null);
                cartActionView = null;
                txtCartBadgeMenu = null;
                actionItem.setIcon(R.drawable.ic_notification);
                actionItem.setTitle(R.string.notification);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /// lấy id của các item
        int id = item.getItemId();
        if (id == R.id.notification) {
            if (currentTabPosition == 1) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            }
            Toast.makeText(this, getString(R.string.toast_notification), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int initialTab = intent.getIntExtra("extra_initial_tab", currentTabPosition);
        if (viewPagerHome != null) {
            viewPagerHome.setCurrentItem(initialTab, false);
        }
        currentTabPosition = initialTab;
        if (toolbarHomePageScreen != null) {
            toolbarHomePageScreen.setTitle(getToolbarTitleRes(initialTab));
        }
        invalidateOptionsMenu();
    }

    @Override
    public void animateAddToCart(View startView) {
        if (!(startView instanceof android.widget.ImageView) || toolbarHomePageScreen == null || currentTabPosition != 1) {
            return;
        }

        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        startView.getLocationOnScreen(startLocation);
        View targetView = cartActionView != null ? cartActionView : toolbarHomePageScreen;
        targetView.getLocationOnScreen(endLocation);

        int targetX = endLocation[0] + (targetView.getWidth() / 2);
        int targetY = endLocation[1] + (targetView.getHeight() / 2);

        android.widget.ImageView animationView = new android.widget.ImageView(this);
        animationView.setImageDrawable(((android.widget.ImageView) startView).getDrawable());

        ViewGroup rootLayout = (ViewGroup) getWindow().getDecorView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                startView.getWidth(),
                startView.getHeight()
        );
        params.leftMargin = startLocation[0];
        params.topMargin = startLocation[1];
        rootLayout.addView(animationView, params);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(animationView, View.SCALE_X, 1f, 4f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(animationView, View.SCALE_Y, 1f, 4f);
        AnimatorSet scaleUpSet = new AnimatorSet();
        scaleUpSet.playTogether(scaleUpX, scaleUpY);
        scaleUpSet.setDuration(220);

        ObjectAnimator translateX = ObjectAnimator.ofFloat(
                animationView,
                View.TRANSLATION_X,
                targetX - startLocation[0]
        );
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
                animationView,
                View.TRANSLATION_Y,
                targetY - startLocation[1]
        );
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(animationView, View.SCALE_X, 4f, 0.3f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(animationView, View.SCALE_Y, 4f, 0.3f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(animationView, View.ALPHA, 1f, 0f);

        AnimatorSet moveToCartSet = new AnimatorSet();
        moveToCartSet.playTogether(translateX, translateY, scaleDownX, scaleDownY, fadeOut);
        moveToCartSet.setDuration(420);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(scaleUpSet, moveToCartSet);
        finalSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rootLayout.removeView(animationView);
            }
        });
        finalSet.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CartManager.addOnCartChangedListener(cartChangedListener);
    }

    @Override
    protected void onStop() {
        CartManager.removeOnCartChangedListener(cartChangedListener);
        super.onStop();
    }

    private void updateCartBadge(int totalQuantity) {
        if (txtCartBadgeMenu == null) {
            return;
        }
        if (totalQuantity <= 0) {
            txtCartBadgeMenu.setVisibility(View.GONE);
            return;
        }
        txtCartBadgeMenu.setVisibility(View.VISIBLE);
        txtCartBadgeMenu.setText(totalQuantity > 99 ? "99+" : String.valueOf(totalQuantity));
    }
}
