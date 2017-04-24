package com.example.yechy.tvass.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.injector.component.ActivityComponent;
import com.example.yechy.tvass.injector.component.DaggerActivityComponent;
import com.example.yechy.tvass.injector.module.ActivityModule;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yechy on 2017/4/3.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;

    public abstract @LayoutRes
    int getLayoutResId();
    public abstract void initInject();
    public abstract void initData();
    public abstract void configViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        initInject();
        initData();
        configViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(App.getInstance().getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
