package com.example.yechy.tvass.communication;

import io.reactivex.Observable;

/**
 * Created by yechy on 2017/4/22.
 */

public interface ICommModel {
    Observable searchDevice(int ip);
    Observable connectDevice(String ip, int port);
    Observable sendKeyCode(int keyCode);
}
