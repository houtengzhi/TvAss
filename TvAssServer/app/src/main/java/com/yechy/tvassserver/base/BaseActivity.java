package com.yechy.tvassserver.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.yechy.tvassserver.util.L;

import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * Created by yechy on 2017/4/3.
 */

public abstract class BaseActivity extends Activity {
    private String TAG = getPageTag();
    private Unbinder unbinder;

    public abstract @LayoutRes
    int getLayoutResId();

    public abstract void initData();
    public abstract void configViews();
    protected abstract String getPageTag();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate()");
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        onViewCreated();
        initData();
        configViews();
    }

    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy()");
        unbinder.unbind();
    }

}
