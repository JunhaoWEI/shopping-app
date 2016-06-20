package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.iot.ecjtu.targets.fragment.OrderSuccessDialog;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.model.Product;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOut2Activity extends AppCompatActivity {
    private Product mProduct;
    private Customer mCustomer;
    private List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out2);

        setToolbar();
        displayProductInfo();
        displayDefaultAddress();

        Button button = (Button) findViewById(R.id.bt_order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderState();
                new OrderSuccessDialog().show(getSupportFragmentManager(), "ordersuccess");
            }
        });
    }

    private void updateOrderState() {
        accountCheck();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.ORDERSTATE;

        for(int i = 0;i<mProduct.getProducts().size();i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", mCustomer.getEmail());
            map.put("id", String.valueOf(mProduct.getProducts().get(i).getId()));
            map.put("quantity","1");
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            map.put("orderTime", date);
            Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            },map);
            mQueue.add(request);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("结算");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void displayProductInfo() {
        double total = 0;
        Intent intent = getIntent();
        mProduct = (Product) getIntent().getSerializableExtra("Product");
        for(int i =0;i<mProduct.getProducts().size();i++){
            total = total+mProduct.getProducts().get(i).getPrice();
        }

        ((TextView) findViewById(R.id.tv_subtotal)).setText(String.valueOf(total));
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

    public void displayDefaultAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.GETDEFAULTADDRESS;
        accountCheck();
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", mCustomer.getEmail());
        Log.i("Checkoutactivity", mCustomer.getEmail());
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("checkout", "success");
                try {
                    ((TextView) findViewById(R.id.tv_address)).setText(response.getString("address"));
                    ((TextView) findViewById(R.id.tv_state_city)).setText(response.getString("state") + "," + response.getString("city"));
                    ((TextView) findViewById(R.id.tv_phone)).setText(response.getInt("phoneNumber") + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("checkout", "failed");
            }
        }, map);
        requestQueue.add(request);
    }
}
