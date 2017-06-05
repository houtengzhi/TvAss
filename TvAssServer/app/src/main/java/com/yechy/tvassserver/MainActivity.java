package com.yechy.tvassserver;

import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yechy.tvassserver.base.BaseActivity;
import com.yechy.tvassserver.service.ListenerService;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.switch_service)
    Switch switchService;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        switchService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(MainActivity.this, ListenerService.class);
                if (isChecked) {
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
        switchService.requestFocus();
    }

    @Override
    protected String getPageTag() {
        return null;
    }

}
