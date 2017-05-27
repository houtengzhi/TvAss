package com.yechy.tvassserver.base;


import com.yechy.tvassserver.App;
import com.yechy.tvassserver.injector.component.ActivityComponent;
import com.yechy.tvassserver.injector.component.DaggerActivityComponent;
import com.yechy.tvassserver.injector.module.ActivityModule;

import javax.inject.Inject;

/**
 * Created by yechy on 2017/5/15.
 */

public abstract class BaseRVActivity<T extends BaseContract.IPresenter> extends BaseActivity
        implements BaseContract.IView {

    @Inject
    protected T mPresenter;

    public abstract void initInject();

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
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
