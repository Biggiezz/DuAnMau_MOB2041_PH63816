package com.example.DuAnMau_PH63816.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.CategoryManagementScreen;
import com.example.DuAnMau_PH63816.common.BottomTabHost;
import com.example.DuAnMau_PH63816.customer.CustomerManagementScreen;
import com.example.DuAnMau_PH63816.invoice.InvoiceActivity;
import com.example.DuAnMau_PH63816.product.ProductScreen;
import com.example.DuAnMau_PH63816.staff.StaffManagementScreen;
import com.example.DuAnMau_PH63816.statistics.StatisticalScreen;
import com.example.DuAnMau_PH63816.top_customer.TopCustomerBuyingProductsScreen;
import com.example.DuAnMau_PH63816.top_product.TopSellingProductsScreen;

public class HomeContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindProductNavigation(view);
        bindNavigation(view, R.id.imgCategory, CategoryManagementScreen.class);
        bindNavigation(view, R.id.imgCustomer, CustomerManagementScreen.class);
        bindNavigation(view, R.id.imgPersonnel, StaffManagementScreen.class);
        bindNavigation(view, R.id.imgInvoice, InvoiceActivity.class);
        bindNavigation(view, R.id.imgTopCustomer, TopCustomerBuyingProductsScreen.class);
        bindNavigation(view, R.id.imgStatistical, StatisticalScreen.class);
        bindNavigation(view, R.id.imgBestSelling, TopSellingProductsScreen.class);
    }

    private void bindProductNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgProduct);
        imageView.setOnClickListener(v -> {
            if (requireActivity() instanceof BottomTabHost) {
                ((BottomTabHost) requireActivity()).openBottomTab(1);
                return;
            }
            startActivity(new Intent(requireContext(), ProductScreen.class));
        });
    }

    private void bindNavigation(View root, int viewId, Class<?> destination) {
        ImageView imageView = root.findViewById(viewId);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), destination)));
    }
}
