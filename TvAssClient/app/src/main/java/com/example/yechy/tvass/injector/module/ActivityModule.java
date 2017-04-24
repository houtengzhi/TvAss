package com.example.yechy.tvass.injector.module;

import android.app.Activity;
import android.content.Context;

import com.example.yechy.tvass.injector.scope.ContextLife;
import com.example.yechy.tvass.injector.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/3.
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideContext() {
        return activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return activity;
    }
}
