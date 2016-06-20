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
import com.iot.ecjtu.targets.model.Picture;
import com.iot.ecjtu.targets.model.Product;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/9.
 */
public class CleverAdapter extends RecyclerView.Adapter<CleverAdapter.ViewHolder> {
    private Context mContext;
    private List<Picture> mPictures;

    public CleverAdapter(Context context,List<Picture> pictures){
        mContext= context;
        mPictures = pictures;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindProduct(mPictures.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.image);
        }
        public void bindProduct(Picture picture) {

            setImg(picture.getImg());
        }
        public void setImg(String image){
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
}
