package com.iot.ecjtu.targets.fragment.signedInFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iot.ecjtu.targets.MainActivity;
import com.iot.ecjtu.targets.R;

/**
 * Created by 497908738@qq.com on 2016/3/11.
 */
public class ProfileFragment extends Fragment {

    private TextView mTextName, mTextEmail, mTextPassword, mTextSignOut;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_profile, container, false);
        // ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextEmail = (TextView) rootView.findViewById(R.id.tv_email);
        mTextName = (TextView) rootView.findViewById(R.id.tv_name);
        mTextPassword = (TextView) rootView.findViewById(R.id.tv_password);
        mTextSignOut = (TextView) rootView.findViewById(R.id.tv_sign_out);

        mSharedPreferences = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String email = mSharedPreferences.getString("email", null);
        String password = mSharedPreferences.getString("password", null);
        String username = mSharedPreferences.getString("username", null);

        mTextEmail.setText(email);
        mTextPassword.setText(password);
        mTextName.setText(username);
        mTextSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("username");
                editor.commit();// 提交修改
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.updateFragment();
            }
        });
        Log.i("onCreatView", "onCreatView");
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i("onCreat", "onCreat");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart", "onstart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPause", "onPause");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("onDetach", "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy");
    }
}
