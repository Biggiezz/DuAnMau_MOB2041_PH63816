package com.example.DuAnMau_PH63816.product;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductScreen extends AppCompatActivity implements BottomTabHost, CartAnimationHost {

    private ViewPager2 viewPagerProduct;
    private View layoutCartAction;
    private TextView txtCartBadge;
    private final CartManager.OnCartChangedListener cartChangedListener = this::updateCartBadge;

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
        layoutCartAction = findViewById(R.id.layoutCartAction);
        ImageView imgCartProduct = findViewById(R.id.imgCartProduct);
        ImageView imgAddProduct = findViewById(R.id.imgAddProduct);
        txtCartBadge = findViewById(R.id.txtCartBadge);
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

        View.OnClickListener openCartClickListener =
                v -> startActivity(new Intent(ProductScreen.this, CartActivity.class));
        layoutCartAction.setOnClickListener(openCartClickListener);
        imgCartProduct.setOnClickListener(openCartClickListener);
        imgAddProduct.setOnClickListener(v -> startActivity(new Intent(ProductScreen.this, AddProductScreen.class)));
        updateCartBadge(CartManager.getTotalQuantity());
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
        if (txtCartBadge == null) {
            return;
        }
        if (totalQuantity > 0) {
            txtCartBadge.setVisibility(View.VISIBLE);
            txtCartBadge.setText(String.valueOf(totalQuantity));
            return;
        }
        txtCartBadge.setVisibility(View.GONE);
    }

    public void animateAddToCart(View startView) {
        if (!(startView instanceof ImageView) || layoutCartAction == null) {
            return;
        }

        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        startView.getLocationOnScreen(startLocation);
        layoutCartAction.getLocationOnScreen(endLocation);

        ImageView animationView = new ImageView(this);
        animationView.setImageDrawable(((ImageView) startView).getDrawable());

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
                endLocation[0] - startLocation[0]
        );
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
                animationView,
                View.TRANSLATION_Y,
                endLocation[1] - startLocation[1]
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
}
