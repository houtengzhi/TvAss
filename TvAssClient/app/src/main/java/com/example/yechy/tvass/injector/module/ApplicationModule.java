package com.example.yechy.tvass.injector.module;

import android.content.Context;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.communication.ICommModel;
import com.example.yechy.tvass.communication.net.TcpApi;
import com.example.yechy.tvass.communication.net.UdpApi;
import com.example.yechy.tvass.injector.qualifier.ContextLife;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/3.
 */
@Module
public class ApplicationModule {
    private App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @ContextLife()
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    public App provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    ICommModel provideCommModel(TcpApi tcpApi, UdpApi udpApi) {
        return new CommModel(tcpApi, udpApi);
    }
}
