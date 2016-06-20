package com.iot.ecjtu.targets.fragment;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.SectionsPagerAdapter;
import com.iot.ecjtu.targets.fragment.childFragment.CategoriesFragment;
import com.iot.ecjtu.targets.fragment.childFragment.DealsFragment;
import com.iot.ecjtu.targets.fragment.childFragment.FeaturedFragment;

/**
 * Created by 497908738@qq.com on 2016/3/5.
 */
public class ShopFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop,container,false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
//        mSectionsPagerAdapter.addFragment(new FeaturedFragment(), "活动");
        mSectionsPagerAdapter.addFragment(new DealsFragment(),"推荐");
        mSectionsPagerAdapter.addFragment(new CategoriesFragment(),"分类");

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }
}
