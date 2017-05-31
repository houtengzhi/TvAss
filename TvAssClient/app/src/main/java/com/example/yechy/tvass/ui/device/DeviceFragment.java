package com.example.yechy.tvass.ui.device;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yechy.tvass.R;
import com.example.yechy.tvass.adapter.DeviceAdapter;
import com.example.yechy.tvass.base.BaseRVFragment;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.AppConfig;
import com.example.yechy.tvass.util.L;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yechy on 2017/4/8.
 */

public class DeviceFragment extends BaseRVFragment<DevicePresenter> implements DeviceContract.IView {
    @BindView(R.id.btn_device_search)
    Button btnDeviceSearch;

    @BindView(R.id.recyclerview_device_list)
    RecyclerView deviceRecyclerview;

    private static final String TAG = DeviceFragment.class.getSimpleName();
    private DeviceAdapter mDeviceAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_device;
    }

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews() {
        initDeviceRecyclerView();
        setListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.d(TAG, "onViewCreated()");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        L.d(TAG, "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.d(TAG, "onDetach()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "onDestroyView()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(TAG, "onStart()");
        mPresenter.registerSearchMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy()");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.d(TAG, "onStop()");
        mPresenter.stopSearchDevices();
    }

    private void initDeviceRecyclerView() {
        mDeviceAdapter = new DeviceAdapter(new ArrayList<Device>(0), getActivity());
        deviceRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        deviceRecyclerview.setAdapter(mDeviceAdapter);

        mDeviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                L.d(TAG, "onItemClick()");
                Device device = (Device) object;
                mPresenter.connectDevice(device, AppConfig.TCP_PORT);
            }
        });
    }

    public static DeviceFragment newInstance() {
        Bundle args = new Bundle();
        DeviceFragment fragment = new DeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener() {
        Disposable disposable = RxView.clicks(btnDeviceSearch)
                .throttleFirst(10, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        mPresenter.clearDeviceList();
                        mPresenter.startSearchDevices();
                    }
                });
        addSubscribe(disposable);

    }

    @Override
    public void refreshDeviceRecyclerView(ArrayList<Device> deviceArrayList) {
        L.d(TAG, "refreshDeviceRecyclerView()");
        mDeviceAdapter.refreshData(deviceArrayList);
    }

    @Override
    public void setSearchButtonClickable(boolean isClickable) {
        L.d(TAG, "setSearchButtonClickable(), isClickable = " + isClickable);
        btnDeviceSearch.setClickable(isClickable);
    }
}
