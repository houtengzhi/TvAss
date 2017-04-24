package com.example.yechy.tvass;

import android.app.Application;

import com.example.yechy.tvass.injector.component.ApplicationComponent;
import com.example.yechy.tvass.injector.component.DaggerApplicationComponent;
import com.example.yechy.tvass.injector.module.ApplicationModule;

/**
 * Created by yechy on 2017/4/3.
 */

public class App extends Application {
    private static App instance;
    private ApplicationComponent applicationComponent;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initComponent();
    }

    private void initComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
