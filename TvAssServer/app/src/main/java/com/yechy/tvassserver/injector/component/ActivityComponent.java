package com.yechy.tvassserver.injector.component;

import android.app.Activity;
import android.content.Context;

import com.yechy.tvassserver.injector.module.ActivityModule;
import com.yechy.tvassserver.injector.qualifier.ContextLife;
import com.yechy.tvassserver.injector.scope.PerActivity;

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

}
