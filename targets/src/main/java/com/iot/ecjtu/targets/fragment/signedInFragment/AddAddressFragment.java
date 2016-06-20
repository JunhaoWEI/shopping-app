package com.iot.ecjtu.targets.fragment.signedInFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 497908738@qq.com on 2016/3/12.
 */
public class AddAddressFragment extends Fragment {
    String defaultAddress;
    private Customer mCustomer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_address, container, false);


        CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    defaultAddress = "1";
                } else {
                    defaultAddress = null;
                }
            }
        });
        Button button = (Button) rootView.findViewById(R.id.bt_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defaultAddress == "1") {
                    updateDefaultAddress();
                    addAddress();
                }else  {
                    addAddress();
                }
            }
        });

        return rootView;
    }

    private void addAddress() {
        String receiveName = ((EditText) getView().findViewById(R.id.et_receive_name)).getText().toString();
        String shippingAddress = ((EditText) getView().findViewById(R.id.et_shipping_address)).getText().toString();
        String zip = ((EditText) getView().findViewById(R.id.et_zip)).getText().toString();
        String state = ((EditText) getView().findViewById(R.id.et_state)).getText().toString();
        String city = ((EditText) getView().findViewById(R.id.et_city)).getText().toString();
        String phoneNumber = ((EditText) getView().findViewById(R.id.et_phoneNumber)).getText().toString();
        Log.i("收件人", receiveName);
        accountCheck();
        Map<String, String> map = new HashMap<String, String>();
        map.put("receiveName", receiveName);
        map.put("shippingAddress", shippingAddress);
        map.put("zip", zip);
        map.put("state", state);
        map.put("city", city);
        map.put("phoneNumber", phoneNumber);
        //Log.i("checkbox是否打勾",defaultAddress);
        map.put("defaultAddress", defaultAddress);
        map.put("email",mCustomer.getEmail());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.ADDADDRESS;
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

    public void updateDefaultAddress(){
        accountCheck();
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", mCustomer.getEmail());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String httpurl = Constant.BASEURL + Constant.UPDATEDEFAULTADDRESS;
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
