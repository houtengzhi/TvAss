package com.yechy.tvassserver.injector.component;

import android.content.Context;

import com.yechy.tvassserver.injector.module.ServiceModule;
import com.yechy.tvassserver.injector.qualifier.ContextLife;
import com.yechy.tvassserver.injector.scope.PerService;
import com.yechy.tvassserver.service.ListenerService;

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

    void inject(ListenerService listenerService);
}
