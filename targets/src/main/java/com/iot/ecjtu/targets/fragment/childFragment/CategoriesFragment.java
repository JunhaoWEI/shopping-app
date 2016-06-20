package com.iot.ecjtu.targets.fragment.childFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.MainActivity;
import com.iot.ecjtu.targets.ProductActivity;
import com.iot.ecjtu.targets.ProductInfoActivity;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.CategoriesAdapter;
import com.iot.ecjtu.targets.model.Product;
import com.iot.ecjtu.targets.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/5.
 */
public class CategoriesFragment extends Fragment {

    private List<Product> mProductList;
    private Product mProduct;

    /*public static CategoriesFragment getInstance(Bundle bundle){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        categoriesFragment.setArguments(bundle);
        return categoriesFragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        getCategories();
        return rootView;
    }

    private void getCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.CATEGORIES;
        mProductList = new ArrayList<Product>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(httpurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                 for (int i = 0 ;i<response.length();i++){
                     try {
                         JSONObject jsonObject = response.getJSONObject(i);
                         Product product = new Product();
                         product.setCategoriesIcon(jsonObject.getString("categoriesIcon"));
                         product.setCategoriesName(jsonObject.getString("categoriesName"));
                         product.setCategoriesId(jsonObject.getInt("categoriesId"));
                         mProductList.add(product);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
                getRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getRecyclerView(){
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.rv);
       // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(),mProductList);
        recyclerView.setAdapter(categoriesAdapter);

        categoriesAdapter.setOnItemClickLitener(new CategoriesAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                mProduct = mProductList.get(position);
                intent.putExtra("Product",mProduct);
                intent.setClass(getActivity(), ProductActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


}
