package com.yechy.tvassserver.injector.module;


import com.yechy.tvassserver.communication.net.TcpApi;
import com.yechy.tvassserver.communication.net.UdpApi;

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
    UdpApi provideUdpApi() {
        return new UdpApi();
    }


}
