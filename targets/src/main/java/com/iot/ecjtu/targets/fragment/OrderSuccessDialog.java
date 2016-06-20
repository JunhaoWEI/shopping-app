package com.iot.ecjtu.targets.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.iot.ecjtu.targets.R;

/**
 * Created by 497908738@qq.com on 2016/3/15.
 */
public class OrderSuccessDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_order_success, null))
                // Add action buttons
                .setPositiveButton("继续浏览商品", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                    }
                })
                .setNegativeButton("查看订单详情", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OrderSuccessDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
