package com.yechy.tvassserver.base;

/**
 * Created by yechy on 2017/4/3.
 */

public interface BaseContract {
    interface IPresenter<T> {
        void attachView(T view);
        void detachView();
    }

    interface IView {

    }
}
