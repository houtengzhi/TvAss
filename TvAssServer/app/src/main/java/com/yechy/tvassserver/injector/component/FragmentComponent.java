package com.yechy.tvassserver.injector.component;

import android.app.Activity;

import com.yechy.tvassserver.injector.module.FragmentModule;
import com.yechy.tvassserver.injector.scope.PerFragment;

import dagger.Component;

/**
 * Created by yechy on 2017/4/3.
 */
@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {

    Activity getActivity();

}
