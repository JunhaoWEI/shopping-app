package com.iot.ecjtu.targets.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.model.Product;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/16.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Product> mProducts;
    private Context mContext;
    private final int RECEIVED = 0;
    private final int REVIEWED = 1;
    private final int ORDER_SUCCESS = 2;

    public OrderHistoryAdapter(List<Product> products, Context context) {
        mProducts = products;
        mContext = context;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindProduct(mProducts.get(position));
        if(mOnItemClickLitener != null){
            holder.tv_receive_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
            holder.tv_received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderTime, tv_orderNumber, tv_description, tv_price,tv_quantity,tv_receive_success,tv_received,tv_comment;
        public ImageView iv_product;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_orderNumber = (TextView) itemView.findViewById(R.id.tv_order_number);
            tv_orderTime = (TextView) itemView.findViewById(R.id.tv_order_time);
            iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
            tv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tv_receive_success = (TextView) itemView.findViewById(R.id.tv_receive_success);
            tv_received = (TextView) itemView.findViewById(R.id.tv_received);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);

        }

        public void bindProduct(Product product) {
            tv_description.setText(product.getDescription());
            tv_price.setText("总价：￥"+String.valueOf(product.getPrice()*product.getQuantity()));
            tv_orderNumber.setText("订单号:"+String.valueOf(product.getOrderNumber()));
            tv_orderTime.setText(product.getOrderTime());
            tv_quantity.setText("数量:"+product.getQuantity());
            setImg(product.getImage());
            switch (product.getOrderState()){
                case RECEIVED:
                    tv_receive_success.setVisibility(View.VISIBLE);
                    tv_received.setVisibility(View.GONE);
                    tv_comment.setVisibility(View.GONE);
                    return;
                case REVIEWED:
                    tv_receive_success.setVisibility(View.GONE);
                    tv_received.setVisibility(View.VISIBLE);
                    tv_comment.setVisibility(View.GONE);
                    return;
                case ORDER_SUCCESS:
                    tv_receive_success.setVisibility(View.GONE);
                    tv_received.setVisibility(View.GONE);
                    tv_comment.setVisibility(View.VISIBLE);
                    return;
            }
        }

        private void setImg(String image) {
            RequestQueue mQueue = Volley.newRequestQueue(mContext);
            ImageRequest imageRequest = new ImageRequest(image, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    iv_product.setImageBitmap(bitmap);
                }
            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(imageRequest);
        }
    }

}



