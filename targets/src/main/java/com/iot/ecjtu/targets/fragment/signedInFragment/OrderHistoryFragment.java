package com.iot.ecjtu.targets.fragment.signedInFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.AddReviewActivity;
import com.iot.ecjtu.targets.ProductInfoActivity;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.OrderHistoryAdapter;
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

/**
 * Created by 497908738@qq.com on 2016/3/15.
 */
public class OrderHistoryFragment extends Fragment {

    private static final int RECEIVING_CONFIRMATION = 1;
    private Customer mCustomer;
    private List<Product> products;
    private Product mProduct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_history,container,false);
        orderHistory();
        return rootView;
    }

    public void orderHistory() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.ORDERHISTORY;
        products = new ArrayList<Product>();
        accountCheck();
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", mCustomer.getEmail());
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("OrderHistory","connect success");
                try {
                    JSONArray jsonArray = response.getJSONArray("order_history");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setImage(jsonObject.getString("image"));
                        product.setDescription(jsonObject.getString("description"));
                        product.setOrderNumber(jsonObject.getInt("orderNumber"));
                        product.setOrderState(jsonObject.getInt("orderState"));
                        product.setOrderTime(jsonObject.getString("orderTime"));
                        product.setQuantity(jsonObject.getInt("quantity"));
                        product.setId(jsonObject.getInt("id"));
                        products.add(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("OrderHistory","连接失败");
            }
        },map);
        requestQueue.add(request);
    }

    private void getRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rv);
        recyclerView.setLayoutManager(linearLayoutManager);
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(products,getContext());
        recyclerView.setAdapter(orderHistoryAdapter);

        orderHistoryAdapter.setOnItemClickLitener(new OrderHistoryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(getContext());
                mMaterialDialog.setTitle("收货成功!");
                mMaterialDialog.setMessage("是否立即评价？");
                mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        receiveConfirm(position,RECEIVING_CONFIRMATION);

                        Intent intent = new Intent();
                        mProduct = products.get(position);
                        intent.putExtra("Product",mProduct);
                        intent.setClass(getActivity(), AddReviewActivity.class);
                        startActivity(intent);
                        mMaterialDialog.dismiss();


                    }
                });
                mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        receiveConfirm(position,RECEIVING_CONFIRMATION);
                        mMaterialDialog.dismiss();

                    }
                });

                mMaterialDialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public boolean accountCheck() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo", getContext().MODE_PRIVATE);
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

    public void receiveConfirm(int position,int orderState){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.RECEIVINGCONFIRMATION;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderNumber",String.valueOf(products.get(position).getOrderNumber()));
        map.put("orderState",String.valueOf(orderState));
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        },map);
        requestQueue.add(request);
    }
}
