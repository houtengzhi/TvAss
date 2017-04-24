package com.example.yechy.tvass.ui.remoute;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yechy.tvass.R;
import com.example.yechy.tvass.base.BaseRVFragment;
import com.example.yechy.tvass.communication.NetUtil;
import com.example.yechy.tvass.util.KeyEvent;
import com.example.yechy.tvass.widget.SendCommentButton;
import com.example.yechy.tvass.widget.TouchPadView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by yechy on 2017/4/3.
 */

public class RemoteFragment extends BaseRVFragment<RemotePresenter> implements RemoteContract.IView {

    @BindView(R.id.iv_remote_channel)
    ImageView ivRemoteChannel;
    @BindView(R.id.iv_remote_volume)
    ImageView ivRemoteVolume;
    @BindView(R.id.imgBtn_remote_ch_plus)
    ImageButton imgBtnRemoteChPlus;
    @BindView(R.id.imgBtn_remote_ch_minus)
    ImageButton imgBtnRemoteChMinus;
    @BindView(R.id.imgBtn_remote_vol_plus)
    ImageButton imgBtnRemoteVolPlus;
    @BindView(R.id.imgBtn_remote_vol_minus)
    ImageButton imgBtnRemoteVolMinus;
    @BindView(R.id.btn_remote_home)
    Button btnRemoteHome;
    @BindView(R.id.btn_remote_input)
    Button btnRemoteInput;
    @BindView(R.id.btn_remote_red)
    ImageButton btnRemoteRed;
    @BindView(R.id.btn_remote_green)
    ImageButton btnRemoteGreen;
    @BindView(R.id.btn_remote_yellow)
    ImageButton btnRemoteYellow;
    @BindView(R.id.btn_remote_blue)
    ImageButton btnRemoteBlue;
    @BindView(R.id.btn_remote_left)
    ImageButton btnRemoteLeft;
    @BindView(R.id.btn_remote_right)
    ImageButton btnRemoteRight;
    @BindView(R.id.btn_remote_1)
    Button btnRemote1;
    @BindView(R.id.btn_remote_2)
    Button btnRemote2;
    @BindView(R.id.btn_remote_3)
    Button btnRemote3;
    @BindView(R.id.btn_remote_4)
    Button btnRemote4;
    @BindView(R.id.btn_remote_5)
    Button btnRemote5;
    @BindView(R.id.btn_remote_6)
    Button btnRemote6;
    @BindView(R.id.btn_remote_7)
    Button btnRemote7;
    @BindView(R.id.btn_remote_8)
    Button btnRemote8;
    @BindView(R.id.btn_remote_9)
    Button btnRemote9;
    @BindView(R.id.btn_remote_num_back)
    Button btnRemoteNumBack;
    @BindView(R.id.btn_remote_0)
    Button btnRemote0;
    @BindView(R.id.btn_remote_num_ok)
    Button btnRemoteNumOk;
    @BindView(R.id.relativeLayout_num)
    RelativeLayout relativeLayoutNum;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btn_remote_navi_ok)
    Button btnRemoteNaviOk;
    @BindView(R.id.btn_remote_navi_up)
    ImageButton btnRemoteNaviUp;
    @BindView(R.id.btn_remote_navi_down)
    ImageButton btnRemoteNaviDown;
    @BindView(R.id.btn_remote_navi_left)
    ImageButton btnRemoteNaviLeft;
    @BindView(R.id.btn_remote_navi_right)
    ImageButton btnRemoteNaviRight;
    @BindView(R.id.rLayout_remote_navigation)
    RelativeLayout rLayoutRemoteNavigation;
    @BindView(R.id.btn_remote_menu)
    ImageButton btnRemoteMenu;
    @BindView(R.id.btn_remote_mute)
    ImageButton btnRemoteMute;
    @BindView(R.id.btn_remote_back)
    ImageButton btnRemoteBack;
    @BindView(R.id.btn_remote_exit)
    ImageButton btnRemoteExit;
    @BindView(R.id.rlayout_remote_navi)
    RelativeLayout rlayoutRemoteNavi;
    @BindView(R.id.touchPadview)
    TouchPadView touchPadview;
    @BindView(R.id.btnMouseLeft)
    Button btnMouseLeft;
    @BindView(R.id.btnMouseRight)
    Button btnMouseRight;
    @BindView(R.id.relativeLayout_touchpad)
    RelativeLayout relativeLayoutTouchpad;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnSendComment)
    SendCommentButton btnSendComment;
    @BindView(R.id.relativeLayout_msg)
    RelativeLayout relativeLayoutMsg;

    private static final String TAG = RemoteFragment.class.getSimpleName();
    private Unbinder unbinder;
    private Vibrator mVibrator;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_remote;
    }

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initDatas() {
        mVibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void configViews() {
        addSubscribe(RxView.clicks(imgBtnRemoteChPlus)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_CHANNEL_UP)));

        addSubscribe(RxView.clicks(imgBtnRemoteChMinus)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_CHANNEL_DOWN)));

        addSubscribe(RxView.clicks(btnRemote0)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_0)));

        addSubscribe(RxView.clicks(btnRemote1)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_1)));

        addSubscribe(RxView.clicks(btnRemote2)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_2)));

        addSubscribe(RxView.clicks(btnRemote3)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_3)));

        addSubscribe(RxView.clicks(btnRemote4)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_4)));

        addSubscribe(RxView.clicks(btnRemote5)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_5)));

        addSubscribe(RxView.clicks(btnRemote6)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_6)));

        addSubscribe(RxView.clicks(btnRemote7)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_7)));

        addSubscribe(RxView.clicks(btnRemote8)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_8)));

        addSubscribe(RxView.clicks(btnRemote9)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new OnKeyConsumer(KeyEvent.KEYCODE_9)));


    }

    public class OnKeyConsumer implements Consumer {
        private int keyCode;

        public OnKeyConsumer(int keyCode) {
            this.keyCode = keyCode;
        }

        @Override
        public void accept(@NonNull Object o) throws Exception {
            mVibrator.vibrate(50);
            NetUtil.getInstance().sendKey(keyCode);
        }
    }

    public RemoteFragment() {
    }

    public static RemoteFragment newInstance() {
        Bundle args = new Bundle();

        RemoteFragment fragment = new RemoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
