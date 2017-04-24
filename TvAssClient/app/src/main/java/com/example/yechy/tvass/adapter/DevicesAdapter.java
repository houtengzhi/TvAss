package com.example.yechy.tvass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.yechy.tvass.R;
import com.example.yechy.tvass.communication.NetUtil;
import com.example.yechy.tvass.structure.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 凯阳 on 2015/8/24.
 */
public class DevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnItemClickListener onItemClickListener = null;

    private List<DeviceInfo> mDevices;
    private DeviceInfo curDeviceInfo;
    private Context context;

    public DevicesAdapter(Context context) {
        this.context = context;
        mDevices = new ArrayList<>();
        curDeviceInfo = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_device, parent, false);
        return new DeviceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DeviceItemViewHolder viewHolder = (DeviceItemViewHolder) holder;
        DeviceInfo deviceInfo = mDevices.get(position);
        viewHolder.bindDeviceInfo(deviceInfo);
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public void addItem(DeviceInfo deviceInfo) {
        if (!mDevices.contains(deviceInfo)) {
            if (deviceInfo.isSelected) {
                curDeviceInfo = deviceInfo;
            }
            mDevices.add(deviceInfo);
            notifyItemInserted(mDevices.size() - 1);
        }else {
            Log.e("gky","invalid device "+deviceInfo.name+"["+deviceInfo.ip+"]");
        }
    }

    public DeviceInfo removeItem(DeviceInfo deviceInfo) {
        int position = mDevices.indexOf(deviceInfo);
        if (position != -1) {
            mDevices.remove(deviceInfo);
            notifyItemRemoved(position);
            return deviceInfo;
        }
        return null;
    }

    public DeviceInfo removeItem(String ip) {
        DeviceInfo devInfo = null;
        for (DeviceInfo deviceInfo : mDevices) {
            if (deviceInfo.ip.equals(ip)) {
                devInfo = deviceInfo;
                break;
            }
        }
        if (devInfo != null) {
            return removeItem(devInfo);
        }
        return null;
    }

    public DeviceInfo getCurDeviceInfo() {
        return curDeviceInfo;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class DeviceItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tvDeviceName)
        TextView tvDeviceName;
        @BindView(R.id.tvDeviceIp)
        TextView tvDeviceIp;
        @BindView(R.id.ibCheckState)
        ImageButton ibCheckState;

        private DeviceInfo mDeviceInfo;

        public DeviceItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindDeviceInfo(DeviceInfo deviceInfo) {
            mDeviceInfo = deviceInfo;
            tvDeviceName.setText(deviceInfo.name);
            tvDeviceIp.setText(deviceInfo.ip);
            int resId = deviceInfo.isSelected ? R.drawable.btn_check_button_square_on : R.drawable.btn_check_button_square_off;
            ibCheckState.setImageResource(resId);
        }

        public void setSelected(boolean isSelected) {
            mDeviceInfo.isSelected = isSelected;
        }

        @Override
        public void onClick(View v) {
            if (mDeviceInfo.isActivated && !mDeviceInfo.isSelected) {
                mDeviceInfo.isSelected = !mDeviceInfo.isSelected;
                if (mDeviceInfo.isSelected) {
                    NetUtil.getInstance().setIpClient(mDeviceInfo.ip);
                }
                for (DeviceInfo deviceInfo : mDevices) {
                    if (NetUtil.getInstance().getIpClient().equals(deviceInfo.ip)) {
                        deviceInfo.isSelected = true;
                        curDeviceInfo = deviceInfo;
                    }else {
                        deviceInfo.isSelected = false;
                    }
                }
                notifyDataSetChanged();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(tvDeviceName.getText().toString(),
                            tvDeviceIp.getText().toString(), mDeviceInfo.isSelected);
                }
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(String device, String ip, boolean isSelected);
    }
}
