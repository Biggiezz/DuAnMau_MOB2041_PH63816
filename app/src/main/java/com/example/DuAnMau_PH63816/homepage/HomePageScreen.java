package com.example.DuAnMau_PH63816.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomBottomButton;
import com.example.DuAnMau_PH63816.customer.CustomerManagementScreen;
import com.example.DuAnMau_PH63816.product.ProductScreen;
import com.google.android.material.navigation.NavigationView;

public class HomePageScreen extends AppCompatActivity {
    private ImageView imgProduct, imgCategory, imgCustomer, imgPersonnel, imgInvoice, imgTopCustomer, imgStatistical, imgBestSelling;
    private CustomBottomButton btnHome, btnCart, btnNotification, btnSetting;

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
        Toolbar toolbarHomePage = findViewById(R.id.toolbarHomePage);
        imgProduct = findViewById(R.id.imgProduct);
        imgCategory = findViewById(R.id.imgCategory);
        imgCustomer = findViewById(R.id.imgCustomer);
        imgPersonnel = findViewById(R.id.imgPersonnel);
        imgInvoice = findViewById(R.id.imgInvoice);
        imgTopCustomer = findViewById(R.id.imgTopCustomer);
        imgStatistical = findViewById(R.id.imgStatistical);
        imgBestSelling = findViewById(R.id.imgBestSelling);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnProfile);

        imgProduct.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, ProductScreen.class)));
        imgCustomer.setOnClickListener(v -> startActivity(new Intent(HomePageScreen.this, CustomerManagementScreen.class)));

        /// toolbar
        setSupportActionBar(toolbarHomePage);
        toolbarHomePage.setNavigationIcon(R.drawable.btn_menu);

        toolbarHomePage.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /// lấy id của các item
        int id = item.getItemId();
        if (id == R.id.notification) {
            Toast.makeText(this, getString(R.string.toast_notification), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
