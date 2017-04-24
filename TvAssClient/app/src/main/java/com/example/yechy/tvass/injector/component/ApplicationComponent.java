package com.example.yechy.tvass.injector.component;

import android.content.Context;

import com.example.yechy.tvass.injector.module.ApplicationModule;
import com.example.yechy.tvass.injector.scope.ContextLife;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ContextLife
    Context getContext();
}
