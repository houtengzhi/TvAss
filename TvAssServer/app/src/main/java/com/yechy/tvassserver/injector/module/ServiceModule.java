package com.yechy.tvassserver.injector.module;

import android.app.Service;
import android.content.Context;

import com.yechy.tvassserver.injector.qualifier.ContextLife;
import com.yechy.tvassserver.injector.scope.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/3.
 */
@Module
public class ServiceModule {
    private Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context provideContext() {
        return service;
    }
}
