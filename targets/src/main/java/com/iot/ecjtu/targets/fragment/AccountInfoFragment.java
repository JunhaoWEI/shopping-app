package com.iot.ecjtu.targets.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot.ecjtu.targets.R;

/**
 * Created by 497908738@qq.com on 2016/3/11.
 */
public class AccountInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_signed_in, container, false);
        return rootView;
    }
}
