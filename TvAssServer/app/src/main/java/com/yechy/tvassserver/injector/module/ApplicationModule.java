package com.yechy.tvassserver.injector.module;

import android.content.Context;

import com.yechy.tvassserver.App;
import com.yechy.tvassserver.communication.CommModel;
import com.yechy.tvassserver.communication.ICommModel;
import com.yechy.tvassserver.communication.net.TcpApi;
import com.yechy.tvassserver.communication.net.UdpApi;
import com.yechy.tvassserver.injector.qualifier.ContextLife;

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
