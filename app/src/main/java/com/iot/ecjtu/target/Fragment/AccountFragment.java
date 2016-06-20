package com.iot.ecjtu.target.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot.ecjtu.target.R;

/**
 * Created by 497908738@qq.com on 2016/3/4.
 */
public class AccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }
}
