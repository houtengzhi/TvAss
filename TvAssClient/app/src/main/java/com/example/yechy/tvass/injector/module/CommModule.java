package com.example.yechy.tvass.injector.module;

import com.example.yechy.tvass.communication.net.TcpClient;
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
    TcpClient provideTcpClient() {
        return new TcpClient();
    }

    @Provides
    @Singleton
    UdpClient provideUdpClient() {
        return new UdpClient();
    }


}
