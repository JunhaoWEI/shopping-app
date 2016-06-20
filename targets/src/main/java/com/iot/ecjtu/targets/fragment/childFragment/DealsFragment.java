package com.iot.ecjtu.targets.fragment.childFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.ProductInfoActivity;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.RecyclerAdapter;
import com.iot.ecjtu.targets.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/5.
 */
public class DealsFragment extends Fragment {

    private RecyclerView mRecyclerView1, mRecyclerView2, mRecyclerView3;
    String url = Constant.BASEURL+Constant.PRODUCTINFO;
    private List<Product> products;
    private Product mProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deals, container, false);
        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.rv1);
        mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.rv2);
        mRecyclerView3 = (RecyclerView) rootView.findViewById(R.id.rv3);
        getItems();
        return rootView;
    }

    private void getItems() {
        products = new ArrayList<Product>();
        final RequestQueue mQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("TAG", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setName(jsonObject.getString("name"));
                        product.setPrice(jsonObject.getDouble("price"));
                        product.setImage(jsonObject.getString("image"));
                        product.setId(jsonObject.getInt("id"));
                        product.setRate(jsonObject.getInt("rate"));
                        product.setNumber(jsonObject.getInt("number"));
                        product.setDescription(jsonObject.getString("description"));
                        products.add(product);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getRecyclerView(mRecyclerView1);
                    getRecyclerView(mRecyclerView2);
                    getRecyclerView(mRecyclerView3);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
                Product product = new Product();
                product.setName("123");
                product.setPrice(20.0);
                product.setImage("234");
                products.add(product);
                Toast.makeText(getContext(), "网络超时",
                        Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(jsonArrayRequest);
    }


    public void getRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(), products);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, final int position) {

                Intent intent = new Intent();
                mProduct = products.get(position);
                intent.putExtra("Product",mProduct);
                intent.setClass(getActivity(), ProductInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(), position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
