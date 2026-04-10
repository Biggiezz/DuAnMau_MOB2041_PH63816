package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

public class ProductContentFragment extends Fragment {

    private final ArrayList<Product> allProducts = new ArrayList<>();
    private final ArrayList<Product> products = new ArrayList<>();
    private ProductDAO productDAO;
    private ProductAdapter productAdapter;
    private RecyclerView rvProducts;
    private FloatingActionButton fabAddProduct;
    private TextView txtEmptyProductState;
    private String currentQuery = "";
    private ProductSearchViewModel productSearchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CartManager.initialize(requireContext());
        productDAO = new ProductDAO(requireContext());
        productSearchViewModel = new ViewModelProvider(requireActivity()).get(ProductSearchViewModel.class);
        initUi(view);
        productSearchViewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            currentQuery = query == null ? "" : query;
            loadProducts();
        });
        loadProducts();
    }

    private void initUi(View view) {
        rvProducts = view.findViewById(R.id.rvProducts);
        fabAddProduct = view.findViewById(R.id.fabAddProduct);
        txtEmptyProductState = view.findViewById(R.id.txtEmptyProductState);

        fabAddProduct.setOnClickListener(v -> startActivity(new Intent(requireContext(), AddProductScreen.class)));

        productAdapter = new ProductAdapter(requireContext(), products);
        rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProducts.setAdapter(productAdapter);
    }

    private void loadProducts() {
        if (productDAO == null || productAdapter == null || txtEmptyProductState == null) {
            return;
        }

        allProducts.clear();
        allProducts.addAll(productDAO.getAllProducts());

        String query = normalizeText(currentQuery);
        String rawQuery = currentQuery.trim();
        products.clear();

        for (Product product : allProducts) {
            boolean isMatch = query.isEmpty();
            if (!isMatch) {
                String name = normalizeText(product.getName());
                String category = normalizeText(product.getCategory());
                String unit = normalizeText(product.getUnit());
                String price = normalizeText(product.getPriceLabel());
                isMatch = name.contains(query) || category.contains(query) || unit.contains(query) || price.contains(query);
            }

            if (isMatch) {
                products.add(product);
            }
        }

        productAdapter.notifyDataSetChanged();

        if (products.isEmpty()) {
            if (rawQuery.isEmpty()) {
                txtEmptyProductState.setText(getString(R.string.product_list_empty));
            } else {
                txtEmptyProductState.setText(getString(R.string.product_search_empty, rawQuery));
            }
            txtEmptyProductState.setVisibility(View.VISIBLE);
        } else {
            txtEmptyProductState.setVisibility(View.GONE);
        }
    }

    public void updateSearchQuery(String query) {
        currentQuery = query == null ? "" : query;
        loadProducts();
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }

        String text = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replace('đ', 'd')
                .replace('Đ', 'D');
        return text.toLowerCase(Locale.getDefault()).trim();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public void onDestroyView() {
        if (productDAO != null) {
            productDAO.close();
            productDAO = null;
        }
        productAdapter = null;
        rvProducts = null;
        fabAddProduct = null;
        txtEmptyProductState = null;
        productSearchViewModel = null;
        super.onDestroyView();
    }
}
