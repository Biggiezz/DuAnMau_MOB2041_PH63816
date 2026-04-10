package com.example.DuAnMau_PH63816.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.common.BottomTabPagerAdapter;
import com.example.DuAnMau_PH63816.product.CartActivity;
import com.example.DuAnMau_PH63816.product.ProductSearchViewModel;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.login.LoginScreen;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomePageScreen extends AppCompatActivity implements BottomTabHost {

    private static final int PRODUCT_TAB_POSITION = 1;

    private DrawerLayout drawerLayout;
    private ViewPager2 viewPagerHome;
    private TabLayout tabLayoutBottom;
    private int currentTabPosition = 0;
    private Toolbar toolbarHomePageScreen;
    private TextView txtCartBadgeMenu;
    private MenuItem searchItem;
    private SearchView searchView;
    private String currentSearchQuery = "";
    private boolean suppressSearchReset;
    private ProductSearchViewModel productSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_screen);
        CartManager.initialize(this);
        productSearchViewModel = new ViewModelProvider(this).get(ProductSearchViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    private void initUi() {
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.navigationView);
        toolbarHomePageScreen = findViewById(R.id.toolbarHomePageScreen);
        tabLayoutBottom = findViewById(R.id.tabLayoutBottom);
        viewPagerHome = findViewById(R.id.viewPagerHome);

        setSupportActionBar(toolbarHomePageScreen);
        toolbarHomePageScreen.setNavigationIcon(R.drawable.btn_menu);
        toolbarHomePageScreen.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        applyRoleUi(navigationView);
        setupBottomTabs();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_log_out) {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupBottomTabs() {
        int initialTab = getIntent().getIntExtra(BottomButtonNavigator.EXTRA_INITIAL_TAB, 0);
        viewPagerHome.setAdapter(new BottomTabPagerAdapter(this));
        viewPagerHome.setCurrentItem(initialTab, false);
        currentTabPosition = initialTab;
        toolbarHomePageScreen.setTitle(getToolbarTitleRes(initialTab));

        new TabLayoutMediator(tabLayoutBottom, viewPagerHome, (tab, position) -> {
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
    }

    private void applyRoleUi(NavigationView navigationView) {
        SharedPreferences sharedPreferences = getSharedPreferences("StaffData", MODE_PRIVATE);
        int currentRole = sharedPreferences.getInt("role", 1);

        if (currentRole == 0) {
            toolbarHomePageScreen.setSubtitle("Quản lý");
        } else if (currentRole == 1) {
            toolbarHomePageScreen.setSubtitle("Nhân viên");
        } else {
            toolbarHomePageScreen.setSubtitle("Chưa xác định vai trò");
        }

        Menu menu = navigationView.getMenu();
        MenuItem shareItem = menu.findItem(R.id.nav_share);
        if (shareItem != null) {
            shareItem.setVisible(currentRole == 0);
        }
    }

    private void logout() {
        getSharedPreferences("StaffData", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        CartManager.clear();

        Intent intent = new Intent(this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
        viewPagerHome.setCurrentItem(position, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        searchItem = menu.findItem(R.id.search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setQueryHint(getString(R.string.product_search_hint));
                searchView.setMaxWidth(Integer.MAX_VALUE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        dispatchSearchQuery(query);
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        currentSearchQuery = newText == null ? "" : newText;
                        dispatchSearchQuery(currentSearchQuery);
                        return true;
                    }
                });
            }

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                    if (suppressSearchReset) {
                        return true;
                    }
                    currentSearchQuery = "";
                    dispatchSearchQuery("");
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isProductTab = currentTabPosition == PRODUCT_TAB_POSITION;
        if (searchItem != null) {
            if (!isProductTab && searchItem.isActionViewExpanded()) {
                suppressSearchReset = true;
                searchItem.collapseActionView();
                suppressSearchReset = false;
            }
            searchItem.setVisible(isProductTab);
            if (isProductTab && searchView != null) {
                CharSequence currentViewQuery = searchView.getQuery();
                String viewQuery = currentViewQuery == null ? "" : currentViewQuery.toString();
                if (!TextUtils.equals(viewQuery, currentSearchQuery)) {
                    searchView.setQuery(currentSearchQuery, false);
                }
                if (!currentSearchQuery.isEmpty() && !searchItem.isActionViewExpanded()) {
                    searchItem.expandActionView();
                    searchView.setQuery(currentSearchQuery, false);
                }
            }
        }

        MenuItem actionItem = menu.findItem(R.id.notification);
        if (actionItem == null) {
            return super.onPrepareOptionsMenu(menu);
        }

        if (isProductTab) {
            if (actionItem.getActionView() == null) {
                actionItem.setActionView(R.layout.view_toolbar_cart_action);
            }

            View cartActionView = actionItem.getActionView();
            txtCartBadgeMenu = cartActionView.findViewById(R.id.txtCartBadgeMenu);
            cartActionView.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
            actionItem.setTitle(R.string.title_cart);
            updateCartBadge();
        } else {
            actionItem.setActionView(null);
            actionItem.setIcon(R.drawable.ic_notification);
            actionItem.setTitle(R.string.notification);
            txtCartBadgeMenu = null;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.notification) {
            if (currentTabPosition == PRODUCT_TAB_POSITION) {
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
        int initialTab = intent.getIntExtra(BottomButtonNavigator.EXTRA_INITIAL_TAB, currentTabPosition);
        if (viewPagerHome != null) {
            viewPagerHome.setCurrentItem(initialTab, false);
        }
        currentTabPosition = initialTab;
        if (toolbarHomePageScreen != null) {
            toolbarHomePageScreen.setTitle(getToolbarTitleRes(initialTab));
        }
        invalidateOptionsMenu();
    }

    private void dispatchSearchQuery(String query) {
        currentSearchQuery = query == null ? "" : query;
        if (productSearchViewModel != null) {
            productSearchViewModel.setSearchQuery(currentSearchQuery);
        }
    }

    private void updateCartBadge() {
        if (txtCartBadgeMenu == null) {
            return;
        }
        int totalQuantity = CartManager.getTotalQuantity();
        if (totalQuantity <= 0) {
            txtCartBadgeMenu.setVisibility(View.GONE);
            return;
        }
        txtCartBadgeMenu.setVisibility(View.VISIBLE);
        txtCartBadgeMenu.setText(totalQuantity > 99 ? "99+" : String.valueOf(totalQuantity));
    }
}
