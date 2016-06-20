package com.iot.ecjtu.targets;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 497908738@qq.com on 2016/3/8.
 */
public class Post {
    private int i;
    private Context mContext;
    private String httpurl = "http://192.168.1.103:8080/Test2/ServletDemo2";

    public Post(int i,Context context){
        this.i = i;
        mContext = context;
    }

    StringRequest stringRequest = new StringRequest(Request.Method.POST,httpurl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // Log.d(TAG, "response -> " + response);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Log.e(TAG, error.getMessage(), error);
        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            //在这里设置需要post的参数
            Map<String, String> map = new HashMap<String, String>();
            map.put("name1", "i");
            return map;
        }
    };

}
