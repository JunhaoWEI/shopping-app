package com.iot.ecjtu.targets.fragment.signedInFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.iot.ecjtu.targets.MainActivity;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.adapter.AddressAdapter;
import com.iot.ecjtu.targets.fragment.DeleteDialog;
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
 * Created by 497908738@qq.com on 2016/3/12.
 */
public class ShippingAddressFragment extends Fragment {

    private List<Customer> mCustomers;
    private Customer mCustomer;
    private RecyclerView mRecyclerView;
    private AddressAdapter mAddressAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipping_address, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        displayAddress();


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).StartNewFragment(new AddAddressFragment());
            }
        });
        return rootView;

    }

    private void displayAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.SHIPPINGADDRESS;
        mCustomers = new ArrayList<Customer>();
        Map<String, String> map = new HashMap<String, String>();
        accountCheck();
        map.put("email", mCustomer.getEmail());
        //Log.i("email", mCustomer.getEmail());
        Request<JSONObject> request = new NormalPostRequest(httpurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("shipping");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Customer customer = new Customer();
                        customer.setAddress(jsonObject.getString("address"));
                        customer.setReceiveName(jsonObject.getString("receiveName"));
                        customer.setCity(jsonObject.getString("city"));
                        customer.setState(jsonObject.getString("state"));
                        customer.setZip(jsonObject.getInt("zip"));
                        customer.setDefaultAddress(jsonObject.getInt("default"));
                        customer.setPhoneNumber(jsonObject.getInt("phoneNumber"));
                        customer.setAddressId(jsonObject.getInt("addressId"));
                        mCustomers.add(customer);

                    }
                } catch (JSONException e) {
                }
                Log.i("=======", "连接成功" + mCustomers.size() + "");
                getRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("=======", "未连接服务器");
            }
        }, map);
        requestQueue.add(request);
    }

    private void getRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAddressAdapter = new AddressAdapter(getContext(), mCustomers);
        mRecyclerView.setAdapter(mAddressAdapter);

        mAddressAdapter.setOnItemClickLitener(new AddressAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ((MainActivity) getActivity()).StartNewFragment(new AddAddressFragment());
            }

            @Override
            public void onItemLongClick(View view, final int position) {
               // new DeleteDialog().show(getFragmentManager(), "DELETE");
                final MaterialDialog mMaterialDialog = new MaterialDialog(getContext());
                mMaterialDialog.setMessage("将要删除此地址？");
               // mMaterialDialog.setMessage("Hello world!");
                mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAddress(position);
                        mAddressAdapter.removeData(position);
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

    public boolean accountCheck() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo", getContext().MODE_PRIVATE);
        mCustomer = new Customer();
        mCustomer.setEmail(sharedPreferences.getString("email", null));
        // Log.i("TAG", sharedPreferences.getString("username", null));
        if (mCustomer.getEmail() == null) {
            return false;
        } else {
            mCustomer.setName(sharedPreferences.getString("username", null));
            mCustomer.setPassword(sharedPreferences.getString("password", null));
            return true;
        }
    }

    public void deleteAddress(int position){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.DELETEADDRESS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("addressId", String.valueOf(mCustomers.get(position).getAddressId()));
       // Log.i("要删除地址的id",String.valueOf(mCustomer.getAddressId()));
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
