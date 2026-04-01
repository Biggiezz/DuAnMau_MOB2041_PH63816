package com.example.DuAnMau_PH63816.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.CategoryManagementScreen;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.customer.CustomerManagementScreen;
import com.example.DuAnMau_PH63816.invoice.InvoiceActivity;
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
        bindCategoryNavigation(view);
        bindCustomerNavigation(view);
        bindPersonnelNavigation(view);
        bindInvoiceNavigation(view);
        bindTopCustomerNavigation(view);
        bindStatisticalNavigation(view);
        bindBestSellingNavigation(view);
    }

    private void bindProductNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgProduct);
        imageView.setOnClickListener(v -> openProductTab());
    }

    private void bindCategoryNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgCategory);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), CategoryManagementScreen.class)));
    }

    private void bindCustomerNavigation(View root) {
        View container = root.findViewById(R.id.layoutCustomer);
        if (!isAdmin()) {
            hideAdminContainer(container);
            return;
        }
        ImageView imageView = root.findViewById(R.id.imgCustomer);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), CustomerManagementScreen.class)));
    }

    private void bindPersonnelNavigation(View root) {
        View container = root.findViewById(R.id.layoutPersonnel);
        if (!isAdmin()) {
            hideAdminContainer(container);
            return;
        }
        ImageView imageView = root.findViewById(R.id.imgPersonnel);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), StaffManagementScreen.class)));
    }

    private void bindInvoiceNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgInvoice);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), InvoiceActivity.class)));
    }

    private void bindTopCustomerNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgTopCustomer);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), TopCustomerBuyingProductsScreen.class)));
    }

    private void bindStatisticalNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgStatistical);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), StatisticalScreen.class)));
    }

    private void bindBestSellingNavigation(View root) {
        ImageView imageView = root.findViewById(R.id.imgBestSelling);
        imageView.setOnClickListener(v -> startActivity(new Intent(requireContext(), TopSellingProductsScreen.class)));
    }

    private boolean isAdmin() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("StaffData", android.content.Context.MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", 1);
        return role == 0;
    }

    private void hideAdminContainer(View container) {
        ViewGroup parent = (ViewGroup) container.getParent();
        if (parent instanceof GridLayout) {
            parent.removeView(container);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    private void openProductTab() {
        Intent intent = new Intent(requireContext(), HomePageScreen.class);
        intent.putExtra(BottomButtonNavigator.EXTRA_INITIAL_TAB, BottomButtonNavigator.TAB_PRODUCT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
