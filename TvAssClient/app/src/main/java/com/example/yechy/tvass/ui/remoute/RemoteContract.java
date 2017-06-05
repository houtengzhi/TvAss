package com.example.yechy.tvass.ui.remoute;

import com.example.yechy.tvass.base.BaseContract;

/**
 * Created by yechy on 2017/4/4.
 */

public interface RemoteContract {
    interface IView extends BaseContract.IView {

    }

    interface IPresenter<T> extends BaseContract.IPresenter<T> {
        void sendKeyCode(int keyCode, byte keyStatus);
    }
}
