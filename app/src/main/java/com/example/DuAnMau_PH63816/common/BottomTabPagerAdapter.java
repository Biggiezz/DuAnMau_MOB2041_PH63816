package com.example.DuAnMau_PH63816.common;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.DuAnMau_PH63816.homepage.HomeContentFragment;
import com.example.DuAnMau_PH63816.notification.fragment.NotificationContentFragment;
import com.example.DuAnMau_PH63816.profile.ProfileContentFragment;
import com.example.DuAnMau_PH63816.product.ProductContentFragment;

public class BottomTabPagerAdapter extends FragmentStateAdapter {

    public BottomTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> new HomeContentFragment();
            case 1 -> new ProductContentFragment();
            case 2 -> new NotificationContentFragment();
            default -> new ProfileContentFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
