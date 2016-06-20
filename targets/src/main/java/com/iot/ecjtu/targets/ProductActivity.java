package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.adapter.ProductAdapter;
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

public class ProductActivity extends AppCompatActivity {

    private List<Product> mProducts;
    private Product mProduct;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        setToolbar();
        getProduct();
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
                            Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                            startActivity(intent);
                        } else {
                            //new SigninDialogFragment().show(getFragmentManager(),"signin");
                            final MaterialDialog mMaterialDialog = new MaterialDialog(ProductActivity.this);
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
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void getProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.PRODUCT;
        mProducts = new ArrayList<Product>();
        Intent intent = getIntent();
        mProduct = (Product) getIntent().getSerializableExtra("Product");
        Log.i("传来的种类id", String.valueOf(mProduct.getCategoriesId()));
        Map<String, String> map = new HashMap<String, String>();
        map.put("categoriesId", String.valueOf(mProduct.getCategoriesId()));
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "连接成功");
                try {
                    JSONArray jsonArray = response.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setImage(jsonObject.getString("image"));
                        product.setDescription(jsonObject.getString("description"));
                        product.setName(jsonObject.getString("name"));
                        product.setRate(jsonObject.getInt("rate"));
                        product.setNumber(jsonObject.getInt("number"));
                        product.setId(jsonObject.getInt("id"));
                        mProducts.add(product);
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

    public void getRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ProductAdapter productAdapter = new ProductAdapter(this, mProducts);
        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickLitener(new ProductAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                mProduct = mProducts.get(position);
                intent.putExtra("Product", mProduct);
                intent.setClass(ProductActivity.this, ProductInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
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
