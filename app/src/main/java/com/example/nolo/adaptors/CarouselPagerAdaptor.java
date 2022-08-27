package com.example.nolo.adaptors;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nolo.fragments.CarouselImageFragment;

import java.util.List;

public class CarouselPagerAdaptor extends FragmentStateAdapter {
    private List<String> uris;

    public CarouselPagerAdaptor(FragmentActivity fa, List<String> uris) {
        super(fa);
        this.uris = uris;
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment f = new CarouselImageFragment(uris.get(position));
        return f;
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }
}