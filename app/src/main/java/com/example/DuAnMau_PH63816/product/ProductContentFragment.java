package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.ArrayList;

public class ProductContentFragment extends Fragment {

    private final ArrayList<Product> products = new ArrayList<>();
    private ProductDAO productDAO;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productDAO = new ProductDAO(requireContext());

        RecyclerView rvProducts = view.findViewById(R.id.rvProducts);
        adapter = new ProductAdapter(
                requireContext(),
                products,
                product -> {
                    Intent intent = new Intent(requireContext(), DetailProductScreen.class);
                    intent.putExtra(ProductExtras.NAME, product.getName());
                    intent.putExtra(ProductExtras.PRICE, product.getPriceLabel());
                    intent.putExtra(ProductExtras.STOCK, product.getStockLabel());
                    intent.putExtra(ProductExtras.IMAGE, product.getImage());
                    intent.putExtra(ProductExtras.ID, product.getId());
                    intent.putExtra(ProductExtras.CATEGORY, product.getCategory());
                    intent.putExtra(ProductExtras.UNIT, product.getUnit());
                    intent.putExtra(ProductExtras.DATE, product.getDate());
                    intent.putExtra(ProductExtras.STATUS, product.getStatus());
                    startActivity(intent);
                },
                (product, addIconView) -> {
                    CartManager.addProduct(product);
                    if (requireActivity() instanceof CartAnimationHost) {
                        ((CartAnimationHost) requireActivity()).animateAddToCart(addIconView);
                    }
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.toast_added_to_cart, product.getName()),
                            Toast.LENGTH_SHORT
                    ).show();
                },
                product -> {
                    CartManager.decreaseQuantity(product);
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.toast_removed_from_cart, product.getName()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProducts.setAdapter(adapter);
        loadProducts();
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
        adapter = null;
        super.onDestroyView();
    }

    private void loadProducts() {
        if (productDAO == null || adapter == null) return;
        products.clear();
        products.addAll(productDAO.getAllProducts());
        adapter.notifyDataSetChanged();
    }
}
