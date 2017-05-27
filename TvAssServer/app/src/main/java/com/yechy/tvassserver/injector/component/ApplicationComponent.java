package com.yechy.tvassserver.injector.component;

import android.content.Context;

import com.yechy.tvassserver.communication.CommModel;
import com.yechy.tvassserver.injector.module.ApplicationModule;
import com.yechy.tvassserver.injector.module.CommModule;
import com.yechy.tvassserver.injector.qualifier.ContextLife;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@Singleton
@Component(modules = {ApplicationModule.class, CommModule.class})
public interface ApplicationComponent {

    @ContextLife
    Context getContext();

    CommModel getCommModel();
}
