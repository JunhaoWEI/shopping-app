package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.fragment.OrderSuccessDialog;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.model.Product;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class CheckoutActivity extends AppCompatActivity {

    private Product mProduct;
    private Customer mCustomer;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setToolbar();
        displayProductInfo();
        displayDefaultAddress();
        getQuantity();

        Button button = (Button) findViewById(R.id.bt_order);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderState();
                new OrderSuccessDialog().show(getSupportFragmentManager(), "ordersuccess");
            }
        });

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

    private void updateOrderState() {
        mQueue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.ORDERSTATE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", mCustomer.getEmail());
        map.put("id", String.valueOf(mProduct.getId()));
        TextView quantity = (TextView) findViewById(R.id.tv_quantity);
        map.put("quantity",quantity.getText().toString());
        // DateFormat.getDateInstance().format(new Date());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        map.put("orderTime", date);
        Log.i("time", date);
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }


    private void displayProductInfo() {
        Intent intent = getIntent();
        mProduct = (Product) getIntent().getSerializableExtra("Product");
        mProduct.setQuantity(1);
        ((TextView) findViewById(R.id.tv_description)).setText(mProduct.getDescription());
        ((TextView) findViewById(R.id.tv_price)).setText(String.valueOf(mProduct.getPrice()));
        ((TextView) findViewById(R.id.tv_subtotal)).setText(String.valueOf(mProduct.getPrice()));
        setImg(mProduct.getImage());
    }

    public void setImg(String image) {
        mQueue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(image, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.i("bitmip", "success");
                ((ImageView) findViewById(R.id.iv_product)).setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("bitmip", "failed");
            }
        });
        mQueue.add(imageRequest);
    }

    public void displayDefaultAddress() {
        mQueue = Volley.newRequestQueue(this);
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
        mQueue.add(request);
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

    public void getQuantity(){
        TextView minus = (TextView) findViewById(R.id.tv_minus);
        final TextView quantity = (TextView)findViewById(R.id.tv_quantity);
        TextView plus = (TextView)findViewById(R.id.tv_plus);
        final TextView subtotal = (TextView) findViewById(R.id.tv_subtotal);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity.getText().toString()) >1) {
                    //mProduct.setQuantity(Integer.parseInt(quantity.getText().toString()) - 1);
                    quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) - 1));

                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mProduct.setQuantity(Integer.parseInt(quantity.getText().toString()) + 1);
                quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) + 1));
                subtotal.setText(String.valueOf(mProduct.getPrice()*Integer.parseInt(quantity.getText().toString())));
            }
        });
    }
}
