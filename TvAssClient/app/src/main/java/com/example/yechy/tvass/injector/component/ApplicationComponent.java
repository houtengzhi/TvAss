package com.example.yechy.tvass.injector.component;

import android.content.Context;

import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.injector.module.ApplicationModule;
import com.example.yechy.tvass.injector.module.CommModule;
import com.example.yechy.tvass.injector.qualifier.ContextLife;
import com.example.yechy.tvass.model.prefs.PreferencesHelperImpl;

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

    PreferencesHelperImpl getPreferencesHelperImpl();
}
