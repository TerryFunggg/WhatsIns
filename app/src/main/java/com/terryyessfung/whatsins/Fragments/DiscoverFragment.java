package com.terryyessfung.whatsins.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.terryyessfung.whatsins.Adapters.DiscoverPagerAdapter;
import com.terryyessfung.whatsins.Helper;
import com.terryyessfung.whatsins.R;

public class DiscoverFragment extends Fragment{


    public DiscoverFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        final TabLayout mTabLayout = view.findViewById(R.id.discover_tabBar);
        final ViewPager mViewPager = view.findViewById(R.id.discover_ViewPager);
        DiscoverPagerAdapter adapter = new DiscoverPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SquareFragment(),getString(R.string.descover_square));
        adapter.addFragment(new SearchFragment(), getString(R.string.descover_search));
        mViewPager.setAdapter(adapter);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });

        return view;
    }

}
