package com.example.yechy.tvass.injector.module;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.yechy.tvass.injector.scope.ContextLife;
import com.example.yechy.tvass.injector.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yechy on 2017/4/3.
 */
@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    @ContextLife("Activity")
    public Context provideContext() {
        return fragment.getActivity();
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    @PerFragment
    public Fragment provideFragment() {
        return fragment;
    }
}
