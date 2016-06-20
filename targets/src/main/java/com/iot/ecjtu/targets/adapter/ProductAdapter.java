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
import com.hedgehog.ratingbar.RatingBar;
import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.model.Product;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/18.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> mProducts;
    private Context mContext;

    public ProductAdapter(Context context, List<Product> products) {
        mContext = context;
        mProducts = products;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindProduct(mProducts.get(position));
        if(mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tv_description, tv_price, tv_number;
        public RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.img);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }

        public void bindProduct(Product product) {
            tv_description.setText(product.getDescription());
            tv_price.setText("￥" + product.getPrice());
            tv_number.setText("(" + product.getNumber() + ")");
            setImg(product.getImage());
            setRate(product.getRate());
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

        public void setRate(int rate) {
            mRatingBar.setStar(rate);
        }
    }
}
