package com.example.yechy.tvass.injector.module;

import android.content.Context;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.injector.scope.ContextLife;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/3.
 */
@Module
public class ApplicationModule {
    private App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @ContextLife()
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    public App provideApplication() {
        return app;
    }
}
