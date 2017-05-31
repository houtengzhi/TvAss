package com.example.yechy.tvass.injector.module;

import android.content.Context;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.communication.ICommModel;
import com.example.yechy.tvass.communication.net.INetModel;
import com.example.yechy.tvass.communication.net.NetModel;
import com.example.yechy.tvass.communication.net.TcpClient;
import com.example.yechy.tvass.communication.net.UdpClient;
import com.example.yechy.tvass.injector.qualifier.ContextLife;
import com.example.yechy.tvass.model.prefs.IPreferencesHelper;
import com.example.yechy.tvass.model.prefs.PreferencesHelperImpl;
import com.example.yechy.tvass.util.AppCookie;

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
    ICommModel provideCommModel(TcpClient tcpClient, UdpClient udpClient, IPreferencesHelper preferencesHelper) {
        return new CommModel(tcpClient, udpClient, preferencesHelper);
    }

    @Provides
    @Singleton
    INetModel provideNetModel(UdpClient udpClient, TcpClient tcpClient) {
        return new NetModel(udpClient, tcpClient);
    }

    @Provides
    @Singleton
    IPreferencesHelper providePreferencesHelper(PreferencesHelperImpl preferencesHelperImpl) {
        return preferencesHelperImpl;
    }

    @Provides
    @Singleton
    AppCookie provideAppCookie(IPreferencesHelper preferencesHelper) {
        return new AppCookie(preferencesHelper);
    }
}
