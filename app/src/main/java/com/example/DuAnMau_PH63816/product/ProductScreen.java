package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.common.BottomTabPagerAdapter;
import com.example.DuAnMau_PH63816.product.data.CartManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductScreen extends AppCompatActivity implements BottomTabHost {

    private static final int PRODUCT_TAB_POSITION = 1;
    private ViewPager2 viewPagerProduct;
    private int currentTabPosition = PRODUCT_TAB_POSITION;
    private TextView txtCartBadgeMenu;
    private MenuItem searchItem;
    private SearchView searchView;
    private String currentSearchQuery = "";
    private boolean suppressSearchReset;
    private ProductSearchViewModel productSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_screen);
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
        Toolbar toolbarProductScreen = findViewById(R.id.toolbarProductScreen);
        TabLayout tabLayout = findViewById(R.id.tabLayoutBottom);
        viewPagerProduct = findViewById(R.id.viewPagerProduct);


        if (toolbarProductScreen != null) {
            setSupportActionBar(toolbarProductScreen);
            toolbarProductScreen.setNavigationOnClickListener(v -> finish());
        }

        viewPagerProduct.setAdapter(new BottomTabPagerAdapter(this));
        viewPagerProduct.setCurrentItem(PRODUCT_TAB_POSITION, false);
        currentTabPosition = PRODUCT_TAB_POSITION;
        if (toolbarProductScreen != null) {
            toolbarProductScreen.setTitle(getToolbarTitleRes(PRODUCT_TAB_POSITION));
        }
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
                currentTabPosition = position;
                toolbarProductScreen.setTitle(getToolbarTitleRes(position));
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_toolbar, menu);
        searchItem = menu.findItem(R.id.action_search_product);
        MenuItem cartItem = menu.findItem(R.id.action_cart_product);

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

        if (cartItem != null) {
            cartItem.setActionView(R.layout.view_toolbar_cart_action);
            View cartActionView = cartItem.getActionView();
            txtCartBadgeMenu = cartActionView != null
                    ? cartActionView.findViewById(R.id.txtCartBadgeMenu)
                    : null;
            if (cartActionView != null) {
                cartActionView.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem cartItem = menu.findItem(R.id.action_cart_product);
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

        if (cartItem != null) {
            cartItem.setVisible(isProductTab);
            updateCartBadge();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart_product) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (totalQuantity > 0) {
            txtCartBadgeMenu.setVisibility(View.VISIBLE);
            txtCartBadgeMenu.setText(totalQuantity > 99 ? "99+" : String.valueOf(totalQuantity));
            return;
        }
        txtCartBadgeMenu.setVisibility(View.GONE);
    }
}
