package com.example.yechy.tvass.injector.component;

import android.content.Context;

import com.example.yechy.tvass.injector.module.ServiceModule;
import com.example.yechy.tvass.injector.scope.ContextLife;
import com.example.yechy.tvass.injector.scope.PerService;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@PerService
@Component(modules = ServiceModule.class, dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife
    Context getApplicationContext();
}
