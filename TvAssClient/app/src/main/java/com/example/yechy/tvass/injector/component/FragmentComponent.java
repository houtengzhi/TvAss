package com.example.yechy.tvass.injector.component;

import android.app.Activity;

import com.example.yechy.tvass.ui.device.DeviceFragment;
import com.example.yechy.tvass.injector.module.FragmentModule;
import com.example.yechy.tvass.injector.scope.PerFragment;
import com.example.yechy.tvass.ui.remoute.RemoteFragment;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(RemoteFragment fragment);

    void inject(DeviceFragment fragment);
}
