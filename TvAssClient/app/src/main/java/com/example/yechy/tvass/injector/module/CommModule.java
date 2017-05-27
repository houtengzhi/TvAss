package com.example.yechy.tvass.injector.module;

import com.example.yechy.tvass.communication.net.TcpApi;
import com.example.yechy.tvass.communication.net.UdpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/27.
 */
@Module
public class CommModule {

    public CommModule() {

    }

    @Provides
    @Singleton
    TcpApi provideTcpApi() {
        return new TcpApi();
    }

    @Provides
    @Singleton
    UdpClient provideUdpApi() {
        return new UdpClient();
    }


}
