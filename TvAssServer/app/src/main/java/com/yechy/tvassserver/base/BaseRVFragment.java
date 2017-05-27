package com.yechy.tvassserver.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yechy.tvassserver.App;
import com.yechy.tvassserver.injector.component.DaggerFragmentComponent;
import com.yechy.tvassserver.injector.component.FragmentComponent;
import com.yechy.tvassserver.injector.module.FragmentModule;
import com.yechy.tvassserver.util.L;
import com.yechy.tvassserver.util.RxBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yechy on 2017/4/3.
 */

public abstract class BaseRVFragment<T extends BaseContract.IPresenter> extends Fragment
        implements BaseContract.IView {
    private String TAG = getPageTag();
    protected View parentView;
    protected FragmentActivity mActivity;
    private Unbinder unbinder;
    protected CompositeDisposable mCompositeDisposable;

    @Inject
    protected T mPresenter;

    public abstract @LayoutRes int getLayoutResId();
    public abstract void initInject();
    public abstract void initDatas();
    public abstract void configViews();
    protected abstract String getPageTag();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        L.d(TAG, "onCreateView()");
        parentView = inflater.inflate(getLayoutResId(), container, false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.d(TAG, "onViewCreated");
        unbinder = ButterKnife.bind(this, view);
        initInject();
        attachView();
        initDatas();
        configViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        L.d(TAG, "onAttach()");
        this.mActivity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.d(TAG, "onDetach");
        this.mActivity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(TAG, "onStart()");
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
    public void onStop() {
        super.onStop();
        L.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "onDestroyView");
        unbinder.unbind();
        unSubscribe();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .applicationComponent(App.getInstance().getApplicationComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
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
