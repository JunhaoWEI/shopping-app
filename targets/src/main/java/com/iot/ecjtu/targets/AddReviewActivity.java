package com.iot.ecjtu.targets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hedgehog.ratingbar.RatingBar;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.model.Product;
import com.iot.ecjtu.targets.model.Review;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AddReviewActivity extends AppCompatActivity {
    private TextView textViewLike;
    private EditText editTextTitle;
    private EditText editTextContent;
    private RatingBar ratingBar;
    private Button btUpdate;
    private Product mProduct;
    private Review mReview;
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        setToolbar();
        init();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("评价");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void init() {
        textViewLike = (TextView) findViewById(R.id.tv_like);
        editTextTitle = (EditText) findViewById(R.id.et_review_title);
        editTextContent = (EditText) findViewById(R.id.et_review_content);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        mReview = new Review();
        ratingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(int RatingCount) {
                        switch (RatingCount) {
                            case 1:
                                textViewLike.setText("差");
                                mReview.setReviewRate(RatingCount);
                                break;
                            case 2:
                                textViewLike.setText("不喜欢");
                                mReview.setReviewRate(RatingCount);
                                break;
                            case 3:
                                textViewLike.setText("还行");
                                mReview.setReviewRate(RatingCount);
                                break;
                            case 4:
                                textViewLike.setText("喜欢");
                                mReview.setReviewRate(RatingCount);
                                break;
                            case 5:
                                textViewLike.setText("超喜欢");
                                mReview.setReviewRate(RatingCount);
                                break;
                        }
                    }
                }
        );
        btUpdate = (Button) findViewById(R.id.bt_update);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReview();
            }
        });
    }

    public void updateReview() {
        mReview.setReviewTitle(editTextTitle.getText().toString());
        mReview.setReviewContent(editTextContent.getText().toString());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mReview.setReviewTime(sDateFormat.format(new java.util.Date()));
        Intent intent = getIntent();
        mProduct = (Product) getIntent().getSerializableExtra("Product");
        accountCheck();


        RequestQueue queue = Volley.newRequestQueue(this);
        String httpurl = Constant.BASEURL + Constant.ADDREVIEW;
        Map<String, String> map = new HashMap<String, String>();
        Log.i("评分",String.valueOf(mReview.getReviewRate()));
        Log.i("说明",mReview.getReviewContent());
        Log.i("产品id",String.valueOf(mProduct.getId()));
        Log.i("账号",mCustomer.getEmail());
        map.put("email", mCustomer.getEmail());
        map.put("username", mCustomer.getName());
        map.put("reviewRate", String.valueOf(mReview.getReviewRate()));
        map.put("reviewTitle", mReview.getReviewTitle());
        map.put("reviewTime", mReview.getReviewTime());
        map.put("reviewContent", mReview.getReviewContent());
        map.put("id",String.valueOf(mProduct.getId()));
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        queue.add(request);


    }

    public boolean accountCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
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
