package com.iot.ecjtu.targets.fragment;


import android.app.Dialog;
import android.app.DialogFragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.util.Constant;
import com.iot.ecjtu.targets.util.NormalPostRequest;
import com.iot.ecjtu.targets.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 497908738@qq.com on 2016/3/13.
 */
public class SigninDialogFragment extends DialogFragment {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private String email;
    private String password;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                // Add action buttons
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        mEmailView = (AutoCompleteTextView) getDialog().findViewById(R.id.email);
                        mPasswordView = (EditText) getDialog().findViewById(R.id.password);
                       // mLoginFormView = getDialog().findViewById(R.id.login_form);
                      //  mProgressView = getDialog().findViewById(R.id.login_progress);
                         email = mEmailView.getText().toString();
                         password = mPasswordView.getText().toString();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SigninDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void init(){
        mEmailView = (AutoCompleteTextView) getDialog().findViewById(R.id.email);
        mPasswordView = (EditText) getDialog().findViewById(R.id.password);
       // mLoginFormView = getDialog().findViewById(R.id.login_form);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

    }

   public void check(){

       String httpurl = Constant.BASEURL+Constant.CHECKACCOUNT;
       StringRequest stringRequest = new StringRequest(Request.Method.POST, httpurl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
                         if (response =="true"){

                         }else {

                         }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map map = new HashMap();
               map.put("email",email);
               map.put("password",password);
               return super.getParams();
           }
       };

   }


}
