package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.adapter.CartAdapter;
import com.iot.ecjtu.targets.adapter.RecyclerAdapter;
import com.iot.ecjtu.targets.model.Customer;
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

import me.drakeet.materialdialog.MaterialDialog;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Product> products;
    private Customer mCustomer;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        accountCheck();
        addToCart(mCustomer.getEmail());
        initBt();

    }

    private void initBt() {
        TextView checkout = (TextView) findViewById(R.id.bt_checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Product",mProduct);
                intent.setClass(CartActivity.this,CheckOut2Activity.class);
                startActivity(intent);
            }
        });
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("购物车");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void addToCart(String account) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.DISPLAYCART;
        mProduct = new Product();
        products = new ArrayList<Product>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", account);
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "连接成功");
                try {
                    JSONArray jsonArray = response.getJSONArray("cart");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setImage(jsonObject.getString("image"));
                        product.setDescription(jsonObject.getString("description"));
                        product.setId(jsonObject.getInt("id"));
                        products.add(product);
                        mProduct.setProducts(products);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAGTAGTAGTAG", "连接失败");
            }
        }, map);
        requestQueue.add(request);
    }

    public boolean accountCheck() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        mCustomer = new Customer();
        mCustomer.setEmail(sharedPreferences.getString("email", null));
        Log.i("TAG", sharedPreferences.getString("username", null));
        if (mCustomer.getEmail() == null) {
            return false;
        } else {
            mCustomer.setName(sharedPreferences.getString("username", null));
            mCustomer.setPassword(sharedPreferences.getString("password", null));
            return true;
        }
    }

    public void getRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.i("***********", products.size() + "");
        final CartAdapter cartAdapter = new CartAdapter(this, products);
        mRecyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnItemClickLitener(new CartAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(CartActivity.this);
                mMaterialDialog.setMessage("从购物车移除此商品？");
                mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAddress(position);
                        cartAdapter.removeData(position);
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();
            }
        });

    }

//    public void onItemRemoved(int position) {
//        Snackbar snackbar = Snackbar.make(
//                findViewById(R.id.container),
//                "商品已删除",
//                Snackbar.LENGTH_LONG);
//
//        snackbar.setAction("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //onItemUndoActionClicked();
//            }
//        });
//        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
//        snackbar.show();
//    }


    public void deleteAddress(int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.DELETEFROMCART;
        Map<String, String> map = new HashMap<String, String>();
        accountCheck();
        map.put("email", mCustomer.getEmail());
        map.put("id", String.valueOf(products.get(position).getId()));
        // Log.i("要删除地址的id",String.valueOf(mCustomer.getAddressId()));
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
