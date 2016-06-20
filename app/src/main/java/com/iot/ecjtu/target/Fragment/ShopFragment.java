package com.iot.ecjtu.target.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot.ecjtu.target.Adapter.SectionsPagerAdapter;
import com.iot.ecjtu.target.Fragment.ChildFragment.CategoriesFragment;
import com.iot.ecjtu.target.Fragment.ChildFragment.DealsFragment;
import com.iot.ecjtu.target.Fragment.ChildFragment.FeaturedFragment;
import com.iot.ecjtu.target.R;

/**
 * Created by 497908738@qq.com on 2016/3/5.
 */
public class ShopFragment extends Fragment{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mSectionsPagerAdapter.addFragment(new FeaturedFragment(),"FEATURED");
        mSectionsPagerAdapter.addFragment(new DealsFragment(),"DEALS");
        mSectionsPagerAdapter.addFragment(new CategoriesFragment(),"CATEGORIES");
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }
}
