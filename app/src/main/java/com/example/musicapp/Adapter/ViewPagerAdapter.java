package com.example.musicapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicapp.Fragment.BlankFragment;
import com.example.musicapp.Fragment.HomeFragment;
import com.example.musicapp.Fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // 4 tab tương trưng cho 0,1,2,3
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new SearchFragment();
            case 2:
                return new BlankFragment();

            case 0:
            default:
                return new HomeFragment();
        }
    }

    // hàm trả về số lượng của tab
    // có 4 tab
    @Override
    public int getCount() {
        return 3;
    }
}
