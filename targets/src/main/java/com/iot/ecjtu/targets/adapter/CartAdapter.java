package com.iot.ecjtu.targets.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 * Created by 497908738@qq.com on 2016/3/14.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> mProducts;
    private Context mContext;

    public CartAdapter(Context context, List<Product> products) {
        mContext = context;
        mProducts = products;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindProduct(mProducts.get(position));
        if(mOnItemClickLitener != null){
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.i("cartadapter",mProducts.size()+"");
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv;
        public TextView tv_description, tv_price;
        public RelativeLayout rl;

        public ViewHolder(View itemView) {
            super(itemView);

            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            iv = (ImageView) itemView.findViewById(R.id.iv_product);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }

        public void bindProduct(Product product) {
            tv_description.setText(product.getDescription());
            tv_price.setText(String.valueOf(product.getPrice()));
            setImg(product.getImage());
        }

        public void setImg(String image) {
            RequestQueue mQueue = Volley.newRequestQueue(mContext);
            ImageRequest imageRequest = new ImageRequest(image, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    iv.setImageBitmap(bitmap);
                }
            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            mQueue.add(imageRequest);
        }
    }
    public void removeData(int position) {
        mProducts.remove(position);
        notifyItemRemoved(position);
    }
}
