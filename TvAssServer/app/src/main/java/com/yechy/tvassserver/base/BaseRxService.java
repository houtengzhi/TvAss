package com.yechy.tvassserver.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yechy.tvassserver.App;
import com.yechy.tvassserver.injector.component.DaggerServiceComponent;
import com.yechy.tvassserver.injector.component.ServiceComponent;
import com.yechy.tvassserver.injector.module.ServiceModule;
import com.yechy.tvassserver.util.RxBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yechy on 2017/4/25.
 */
public abstract class BaseRxService<T extends BaseContract.IPresenter> extends Service
        implements BaseContract.IView{
    protected CompositeDisposable mCompositeDisposable;

    @Inject
    protected T mPresenter;

    protected abstract void initInject();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initInject();
        attachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected ServiceComponent getServiceComponent() {
        return DaggerServiceComponent.builder()
                .applicationComponent(App.getInstance().getApplicationComponent())
                .serviceModule(getServiceModule())
                .build();
    }

    private ServiceModule getServiceModule() {
        return new ServiceModule(this);
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(RxBus.getDefault().toDefaultFlowable(eventType, act));
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
}
