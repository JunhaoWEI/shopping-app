package com.iot.ecjtu.targets;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.iot.ecjtu.targets.fragment.AccountFragment;
import com.iot.ecjtu.targets.fragment.ShopFragment;

import com.iot.ecjtu.targets.fragment.TestFragment;
import com.iot.ecjtu.targets.model.Customer;


import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment isFragment;
    private AccountFragment mAccountFragment;
    private ShopFragment mShopFragment;
    private TestFragment mTestFragment;
    private FragmentManager mFragmentManager;
    private ActionBarDrawerToggle toggle;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_cart:
                        if (accountCheck()) {
                            Intent intent = new Intent(MainActivity.this, CartActivity.class);
                            startActivity(intent);
                            break;
                        } else {
                            // new SigninDialogFragment().show(getFragmentManager(),"signin");
                            final MaterialDialog mMaterialDialog = new MaterialDialog(MainActivity.this);
                            mMaterialDialog.setTitle("请先登入！");
                            // mMaterialDialog.setMessage("Hello world!");
                            mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();


                                }
                            });
                            mMaterialDialog.show();
                            break;
                        }
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        setDefaultFragment();
        initFragment(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    toggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.setDrawerIndicatorEnabled(true);

                }
            }
        });

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //toggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (mShopFragment == null) {
                mShopFragment = new ShopFragment();
                switchContent(isFragment, mShopFragment);
            } else {
                switchContent(isFragment, mShopFragment);
            }

        } else if (id == R.id.nav_gallery) {

            if (mAccountFragment == null) {
                mAccountFragment = new AccountFragment();
                switchContent(isFragment, mAccountFragment);
            } else {
                switchContent(isFragment, mAccountFragment);
            }
        } else if (id == R.id.nav_2dcode) {

            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        }
        /*} else if (id == R.id.nav_slideshow) {

            if (mTestFragment == null) {
                mTestFragment = new TestFragment();
                switchContent(isFragment, mTestFragment);
            } else {
                switchContent(isFragment, mTestFragment);
            }


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mAccountFragment = new AccountFragment();
        transaction.replace(R.id.content, mAccountFragment);
        //transaction.hide(isFragment).show(mAccountFragment);
        isFragment = mAccountFragment;
        transaction.commit();
    }

    public void StartNewFragment(Fragment fragment) {
        // toggle.setDrawerIndicatorEnabled(false);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        isFragment = fragment;
        transaction.commit();
    }

    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (mShopFragment == null) {
                mShopFragment = new ShopFragment();
            }
            isFragment = mShopFragment;
            ft.replace(R.id.content, mShopFragment).commit();
        }
    }

    public void setDefaultFragment() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mShopFragment = new ShopFragment();
        //mAccountFragment = new AccountFragment();
        transaction.replace(R.id.content, mShopFragment);
        transaction.commit();
        isFragment = mShopFragment;
    }

    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                //ft.hide(from).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                ft.replace(R.id.content, to).commit();
            } else {
                //ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
                ft.replace(R.id.content, to).commit();
            }
        }
    }

    public boolean accountCheck() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        mCustomer = new Customer();
        mCustomer.setEmail(sharedPreferences.getString("email", null));
        //Log.i("TAG",sharedPreferences.getString("username",null));
        if (mCustomer.getEmail() == null) {
            return false;
        } else {
            mCustomer.setName(sharedPreferences.getString("username", null));
            mCustomer.setPassword(sharedPreferences.getString("password", null));
            return true;
        }
    }
}