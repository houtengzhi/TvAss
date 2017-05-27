package com.example.yechy.tvass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yechy.tvass.R;
import com.example.yechy.tvass.model.bean.Device;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yechy on 2017/5/24.
 */

public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Device> mDeviceList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DeviceAdapter(ArrayList<Device> deviceList, Context context) {
        this.mDeviceList = deviceList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item_device, parent, false);
        return new DeviceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DeviceItemViewHolder viewHolder = (DeviceItemViewHolder) holder;
        final Device device = mDeviceList.get(position);
        viewHolder.bindDeviceInfo(device);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemClick(viewHolder.itemView, pos, device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public void refreshData(ArrayList<Device> deviceList) {
        mDeviceList.clear();
        mDeviceList.addAll(deviceList);
        notifyDataSetChanged();
    }

    public class DeviceItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_device_item_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_device_item_ip)
        TextView tvDeviceIp;


        public DeviceItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDeviceInfo(Device device) {
            tvDeviceName.setText(device.getName());
            tvDeviceIp.setText(device.getIp() + ":" + device.getPort());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, Object object);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
