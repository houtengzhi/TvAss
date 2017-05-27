package com.yechy.tvassserver;

import android.app.Application;

import com.yechy.tvassserver.injector.component.ApplicationComponent;
import com.yechy.tvassserver.injector.component.DaggerApplicationComponent;
import com.yechy.tvassserver.injector.module.ApplicationModule;


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
