package com.iot.ecjtu.targets.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.ecjtu.targets.MainActivity;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.SectionsPagerAdapter;
import com.iot.ecjtu.targets.fragment.childFragment.SignInFragment;
import com.iot.ecjtu.targets.fragment.childFragment.SignUpFragment;
import com.iot.ecjtu.targets.fragment.signedInFragment.OrderHistoryFragment;
import com.iot.ecjtu.targets.fragment.signedInFragment.ProfileFragment;
import com.iot.ecjtu.targets.fragment.signedInFragment.ShippingAddressFragment;

/**
 * Created by 497908738@qq.com on 2016/3/5.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SharedPreferences mSharedPreferences;

    private RelativeLayout mProfile, mAddress, mOrderHistory;
    private TextView mTextName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSharedPreferences = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String email = mSharedPreferences.getString("email", null);
        String password = mSharedPreferences.getString("password", null);
        String username = mSharedPreferences.getString("username", null);

        if (email == null) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
            mSectionsPagerAdapter.addFragment(new SignInFragment(), "登陆");
            mSectionsPagerAdapter.addFragment(new SignUpFragment(), "注册");

            mViewPager = (ViewPager) rootView.findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            return rootView;
        } else {

            View rootView = inflater.inflate(R.layout.fragment_account_signed_in, container, false);
            mProfile = (RelativeLayout) rootView.findViewById(R.id.rl_profile);
            mAddress = (RelativeLayout) rootView.findViewById(R.id.rl_address);
            mOrderHistory = (RelativeLayout) rootView.findViewById(R.id.rl_order_history);
            mTextName = (TextView) rootView.findViewById(R.id.tv_welcome);
            mTextName.setText("欢迎," + username+"!");

            mProfile.setOnClickListener(this);
            mAddress.setOnClickListener(this);
            mOrderHistory.setOnClickListener(this);
            return rootView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_profile:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.StartNewFragment(new ProfileFragment());
                break;
            case R.id.rl_address:
                ((MainActivity)getActivity()).StartNewFragment(new ShippingAddressFragment());
                break;
            case R.id.rl_order_history:
                ((MainActivity)getActivity()).StartNewFragment(new OrderHistoryFragment());
                break;
        }
    }
}
