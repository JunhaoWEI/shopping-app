package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hedgehog.ratingbar.RatingBar;
import com.iot.ecjtu.targets.adapter.CleverAdapter;
import com.iot.ecjtu.targets.fragment.SigninDialogFragment;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.model.Picture;
import com.iot.ecjtu.targets.model.Product;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luckyandyzhang.cleverrecyclerview.CleverRecyclerView;
import me.drakeet.materialdialog.MaterialDialog;

public class ProductInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Product mProduct;
    private Customer mCustomer;
    private ViewPager viewPager;
    private ImageView mImageView;
    private List<ImageView> mViews;
    private List<Picture> mPictures;
    private LinearLayoutManager mLayoutManager;
    private CleverRecyclerView mRecyclerView;
    private CardView mCardView, mCardView2;
    private Button mBuyItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        setToolbar();
        mRecyclerView = (CleverRecyclerView) findViewById(R.id.cleverv);

        mCardView = (CardView) findViewById(R.id.cv_like);
        mCardView2 = (CardView) findViewById(R.id.cv_add);
        mBuyItem = (Button) findViewById(R.id.bt_buy_item);

        mCardView.setOnClickListener(this);
        mCardView2.setOnClickListener(this);
        mBuyItem.setOnClickListener(this);



        /*RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(int RatingCount) {
                        Toast.makeText(ProductInfoActivity.this, "the fill star is" + RatingCount, Toast.LENGTH_LONG).show();
                    }
                }
        );
        mRatingBar.setStar(mProduct.getRate());
        mRatingBar.setmClickable(false);
        mRatingBar.setStarImageSize(14f);
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));  */

        diaplayProductInfo();
        displayPictures();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_cart:
                        if (accountCheck()) {
                            Intent intent = new Intent(ProductInfoActivity.this, CartActivity.class);
                            startActivity(intent);
                            break;
                        }else {
                            final MaterialDialog mMaterialDialog = new MaterialDialog(ProductInfoActivity.this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_like:
                break;
            case R.id.cv_add:
                if (accountCheck()) {
                    addToCart(mCustomer.getEmail(), mProduct.getId());
                    Snackbar snackbar = Snackbar
                            .make(getCurrentFocus(), "成功加入购物车", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    //new SigninDialogFragment().show(getFragmentManager(),"signin");
                    final MaterialDialog mMaterialDialog = new MaterialDialog(this);
                    mMaterialDialog.setTitle("请先登入！");
                    // mMaterialDialog.setMessage("Hello world!");
                    mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();


                        }
                    });
                    mMaterialDialog.show();
                }
                break;
            case R.id.bt_buy_item:
                if (accountCheck()) {
                    //已登录，跳到购买界面
                    Intent intent = new Intent();
                    intent.putExtra("Product", mProduct);
                    intent.setClass(ProductInfoActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    //未登录，跳出登陆框
                    //new SigninDialogFragment().show(getFragmentManager(),"signin");
                    final MaterialDialog mMaterialDialog = new MaterialDialog(this);
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

    }

    private void diaplayProductInfo() {
        Intent intent = getIntent();
        mProduct = (Product) getIntent().getSerializableExtra("Product");
        ((TextView) findViewById(R.id.tv_description)).setText(mProduct.getDescription());
        ((TextView) findViewById(R.id.tv_price)).setText(mProduct.getPrice() + "");
        ((TextView) findViewById(R.id.tv_number)).setText("(" + mProduct.getNumber() + ")");
        ((RatingBar) findViewById(R.id.ratingbar)).setStar(mProduct.getRate());
    }

    private void displayPictures() {
        mPictures = new ArrayList<Picture>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.PICTURES;
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", mProduct.getId() + "");
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("picture");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Picture picture = new Picture();
                        picture.setImg(jsonObject.getString("img"));
                        mPictures.add(picture);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                //mRecyclerView.setOrientation(RecyclerView.HORIZONTAL);
                mRecyclerView.setAdapter(new CleverAdapter(getApplicationContext(), mPictures));
                ((TextView) findViewById(R.id.tv_picture_positon)).setPadding(15, 15, 15, 15);
                ((TextView) findViewById(R.id.tv_picture_positon)).setText(1 + " of " + mPictures.size());
                mRecyclerView.setOnPageChangedListener(new CleverRecyclerView.OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int i) {
                        ((TextView) findViewById(R.id.tv_picture_positon)).setText(i + 1 + " of " + mPictures.size());
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            }
        }, map);
        requestQueue.add(request);
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


    public void addToCart(String account, int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.ADDTOCART;
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", account);
        map.put("id", id + "");
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

}
