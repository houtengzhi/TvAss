package com.example.yechy.tvass.injector.component;

import android.app.Activity;
import android.content.Context;

import com.example.yechy.tvass.HomeActivity;
import com.example.yechy.tvass.injector.module.ActivityModule;
import com.example.yechy.tvass.injector.scope.ContextLife;
import com.example.yechy.tvass.injector.scope.PerActivity;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife
    Context getApplicationContext();

    Activity getActivity();

    void inject(HomeActivity activity);
}
