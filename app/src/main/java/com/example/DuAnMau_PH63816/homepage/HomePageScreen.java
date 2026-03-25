package com.example.DuAnMau_PH63816.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.CategoryManagementScreen;
import com.example.DuAnMau_PH63816.custom.CustomBottomButton;
import com.example.DuAnMau_PH63816.customer.CustomerManagementScreen;
import com.example.DuAnMau_PH63816.invoice.InvoiceActivity;
import com.example.DuAnMau_PH63816.product.ProductScreen;
import com.example.DuAnMau_PH63816.profile.ProfileScreen;
import com.example.DuAnMau_PH63816.staff.StaffManagementScreen;
import com.example.DuAnMau_PH63816.statistics.StatisticalScreen;
import com.example.DuAnMau_PH63816.top_customer.TopCustomerBuyingProductsScreen;
import com.example.DuAnMau_PH63816.top_product.TopSellingProductsScreen;
import com.google.android.material.navigation.NavigationView;

public class HomePageScreen extends AppCompatActivity {

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
        Toolbar toolbarHomePageScreen = findViewById(R.id.toolbarHomePageScreen);
        ImageView imgProduct = findViewById(R.id.imgProduct);
        ImageView imgCategory = findViewById(R.id.imgCategory);
        ImageView imgCustomer = findViewById(R.id.imgCustomer);
        ImageView imgPersonnel = findViewById(R.id.imgPersonnel);
        ImageView imgInvoice = findViewById(R.id.imgInvoice);
        ImageView imgTopCustomer = findViewById(R.id.imgTopCustomer);
        ImageView imgStatistical = findViewById(R.id.imgStatistical);
        ImageView imgBestSelling = findViewById(R.id.imgBestSelling);
        CustomBottomButton btnHome = findViewById(R.id.btnHome);
        CustomBottomButton btnCart = findViewById(R.id.btnCart);
        CustomBottomButton btnNotification = findViewById(R.id.btnNotification);
        CustomBottomButton btnSetting = findViewById(R.id.btnProfile);

        imgProduct.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, ProductScreen.class)));
        imgCustomer.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, CustomerManagementScreen.class)));
        imgBestSelling.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, TopSellingProductsScreen.class)));
        imgTopCustomer.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, TopCustomerBuyingProductsScreen.class)));
        imgPersonnel.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, StaffManagementScreen.class)));
        imgInvoice.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, InvoiceActivity.class)));
        imgCategory.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, CategoryManagementScreen.class)));
        imgStatistical.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, StatisticalScreen.class)));
        btnSetting.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, ProfileScreen.class)));
        /// toolbar
        setSupportActionBar(toolbarHomePageScreen);
        toolbarHomePageScreen.setNavigationIcon(R.drawable.ic_back);
        toolbarHomePageScreen.setNavigationOnClickListener(v -> finish());

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    /// Cách sử dụng Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /// lấy id của các item
        int id = item.getItemId();
        if (id == R.id.notification) {
            Toast.makeText(this, getString(R.string.toast_notification), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
