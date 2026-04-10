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
        ImageView imgProduct = view.findViewById(R.id.imgProduct);
        ImageView imgCategory = view.findViewById(R.id.imgCategory);
        ImageView imgCustomer = view.findViewById(R.id.imgCustomer);
        ImageView imgPersonnel = view.findViewById(R.id.imgPersonnel);
        ImageView imgInvoice = view.findViewById(R.id.imgInvoice);
        ImageView imgTopCustomer = view.findViewById(R.id.imgTopCustomer);
        ImageView imgStatistical = view.findViewById(R.id.imgStatistical);
        ImageView imgBestSelling = view.findViewById(R.id.imgBestSelling);
        View layoutCustomer = view.findViewById(R.id.layoutCustomer);
        View layoutPersonnel = view.findViewById(R.id.layoutPersonnel);

        imgProduct.setOnClickListener(v -> openProductTab());
        imgCategory.setOnClickListener(v -> startActivity(new Intent(requireContext(), CategoryManagementScreen.class)));
        imgInvoice.setOnClickListener(v -> startActivity(new Intent(requireContext(), InvoiceActivity.class)));
        imgTopCustomer.setOnClickListener(v -> startActivity(new Intent(requireContext(), TopCustomerBuyingProductsScreen.class)));
        imgStatistical.setOnClickListener(v -> startActivity(new Intent(requireContext(), StatisticalScreen.class)));
        imgBestSelling.setOnClickListener(v -> startActivity(new Intent(requireContext(), TopSellingProductsScreen.class)));

        if (isAdmin()) {
            imgCustomer.setOnClickListener(v -> startActivity(new Intent(requireContext(), CustomerManagementScreen.class)));
            imgPersonnel.setOnClickListener(v -> startActivity(new Intent(requireContext(), StaffManagementScreen.class)));
        } else {
            hideAdminContainer(layoutCustomer);
            hideAdminContainer(layoutPersonnel);
        }
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
