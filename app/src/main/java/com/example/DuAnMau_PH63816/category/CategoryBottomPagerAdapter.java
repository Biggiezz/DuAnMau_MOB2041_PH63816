package com.example.DuAnMau_PH63816.category;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.DuAnMau_PH63816.product.ProductContentFragment;
import com.example.DuAnMau_PH63816.notification.fragment.NotificationContentFragment;
import com.example.DuAnMau_PH63816.profile.ProfileContentFragment;

public class CategoryBottomPagerAdapter extends FragmentStateAdapter {

    public CategoryBottomPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> new CategoryContentFragment();
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
