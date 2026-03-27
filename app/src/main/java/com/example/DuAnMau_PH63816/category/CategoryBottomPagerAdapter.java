package com.example.DuAnMau_PH63816.category;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.DuAnMau_PH63816.common.SimpleMessageFragment;
import com.example.DuAnMau_PH63816.product.ProductContentFragment;
import com.example.DuAnMau_PH63816.profile.ProfileContentFragment;

public class CategoryBottomPagerAdapter extends FragmentStateAdapter {

    public CategoryBottomPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CategoryContentFragment();
            case 1:
                return new ProductContentFragment();
            case 2:
                return SimpleMessageFragment.newInstance("Thông báo");
            case 3:
            default:
                return new ProfileContentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
