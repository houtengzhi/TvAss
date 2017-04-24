package com.example.yechy.tvass.ui.device;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.yechy.tvass.R;
import com.example.yechy.tvass.base.BaseRVFragment;
import com.jakewharton.rxbinding2.view.RxView;

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
        setListener();
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

                    }
                });
        addSubscribe(disposable);
    }
}
