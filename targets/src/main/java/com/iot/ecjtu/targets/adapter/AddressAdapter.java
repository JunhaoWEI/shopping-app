package com.iot.ecjtu.targets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iot.ecjtu.targets.R;
import com.iot.ecjtu.targets.model.Customer;
import com.iot.ecjtu.targets.model.Product;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/14.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context mContext;
    private List<Customer> mCustomers;

    public AddressAdapter(Context context, List<Customer> customers) {
        mContext = context;
        mCustomers = customers;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.address.setText(mCustomers.get(position).getAddress());
        holder.city.setText(mCustomers.get(position).getState()+","+mCustomers.get(position).getCity());
        holder.bindProduct(mCustomers.get(position));
        if(mOnItemClickLitener != null){
            holder.edit.setOnClickListener(new View.OnClickListener() {
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
        return mCustomers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView address, city,defaultAddress;
        public ImageView edit;
        public ViewHolder(View itemView) {
            super(itemView);
            edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            city = (TextView) itemView.findViewById(R.id.tv_state_city);
            defaultAddress = (TextView) itemView.findViewById(R.id.tv_default_address);
        }

        public void bindProduct(Customer customer) {
            setDefaultAddress(customer.getDefaultAddress());
        }

        private void setDefaultAddress(int i) {
            if(i != 1){
                defaultAddress.setVisibility(View.GONE);
            }
        }
    }
    public void removeData(int position) {
        mCustomers.remove(position);
        notifyItemRemoved(position);
    }
}
