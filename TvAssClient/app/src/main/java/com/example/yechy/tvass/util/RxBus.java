package com.example.yechy.tvass.util;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by yechy on 2017/4/3.
 */

public class RxBus {
    private final FlowableProcessor<Object> bus;

    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault() {
        return RxBusHolder.instance;
    }

    private static class RxBusHolder {
        private static final RxBus instance = new RxBus();
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    // 封装默认订阅
    public <T>Disposable toDefaultFlowable(Class<T> eventType, Consumer<T> act) {
        return bus.ofType(eventType).compose(RxUtil.<T>rxSchedulerHelper()).subscribe(act);
    }
}
