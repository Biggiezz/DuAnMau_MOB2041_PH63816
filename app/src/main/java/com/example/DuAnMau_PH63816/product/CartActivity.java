package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.homepage.HomePageScreen;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.product.adapter.CartAdapter;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private final List<CartItem> cartItems = new ArrayList<>();
    private RecyclerView rvCartProducts;
    private CartAdapter cartAdapter;
    private TextView txtCartItemCount;
    private TextView txtCartEmpty;
    private TextView txtCartSubtotal;
    private TextView txtCartTotal;
    private View layoutCartSummary;
    private InvoiceDAO invoiceDAO;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        CartManager.initialize(this);
        invoiceDAO = new InvoiceDAO(this);
        productDAO = new ProductDAO(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        setupRecyclerView();
        refreshCart();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbarCart);
        rvCartProducts = findViewById(R.id.rvCartProducts);
        Button btnCheckout = findViewById(R.id.btnCheckout);
        txtCartItemCount = findViewById(R.id.txtCartItemCount);
        txtCartEmpty = findViewById(R.id.txtCartEmpty);
        txtCartSubtotal = findViewById(R.id.txtCartSubtotal);
        txtCartTotal = findViewById(R.id.txtCartTotal);
        layoutCartSummary = findViewById(R.id.layoutCartSummary);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        btnCheckout.setOnClickListener(v -> checkout());
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, cartItems);

        rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCartProducts.setAdapter(cartAdapter);
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
    }

    private void checkout() {
        ArrayList<CartItem> checkoutItems = new ArrayList<>(CartManager.getItems());
        if (checkoutItems.isEmpty()) {
            Toast.makeText(this, getString(R.string.cart_empty), Toast.LENGTH_LONG).show();
            return;
        }

        int createdInvoiceId = invoiceDAO.createPaidInvoiceFromCart(checkoutItems);
        if (createdInvoiceId == -1) {
            Toast.makeText(this, "Thanh toán thất bại, chưa tạo được hóa đơn", Toast.LENGTH_LONG).show();
            return;
        }

        if (!productDAO.applyCheckoutStock(checkoutItems)) {
            invoiceDAO.deleteInvoice(createdInvoiceId);
            Toast.makeText(this, "Thanh toán thất bại, không cập nhật được tồn kho", Toast.LENGTH_LONG).show();
            return;
        }

        CartManager.clear();
        refreshCart();

        Intent intent = new Intent(this, HomePageScreen.class);
        intent.putExtra(BottomButtonNavigator.EXTRA_INITIAL_TAB, BottomButtonNavigator.TAB_NOTIFICATION);
        intent.putExtra("from_checkout", true);
        intent.putExtra("created_invoice_id", createdInvoiceId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCart();
    }

    public void refreshCart() {
        cartItems.clear();
        cartItems.addAll(CartManager.getItems());
        cartAdapter.notifyDataSetChanged();

        int totalQuantity = CartManager.getTotalQuantity();
        boolean hasItems = !cartItems.isEmpty();
        txtCartItemCount.setText(getString(R.string.cart_item_count_format, totalQuantity));
        ///
        txtCartEmpty.setVisibility(hasItems ? View.GONE : View.VISIBLE);
        ///
        layoutCartSummary.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        txtCartSubtotal.setText(CartManager.formatCurrency(CartManager.getSubtotal()));
        txtCartTotal.setText(CartManager.formatCurrency(CartManager.getSubtotal()));
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        if (invoiceDAO != null) {
            invoiceDAO.close();
        }
        super.onDestroy();
    }
}
