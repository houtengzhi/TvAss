package com.example.yechy.tvass.ui.remoute;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yechy.tvass.R;
import com.example.yechy.tvass.base.BaseRVFragment;
import com.example.yechy.tvass.model.keytable.AGKeyCode;
import com.example.yechy.tvass.util.AppCookie;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.widget.SendCommentButton;
import com.example.yechy.tvass.widget.TouchPadView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

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
    private OnMockKeyListener onMockKeyListener = new OnMockKeyListener();
    private Disposable timerDisposable;

    @Inject
    public AppCookie appCookie;

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
        imgBtnRemoteChPlus.setTag(R.id.tag_keycode, AGKeyCode.CHANNEL_UP);
        imgBtnRemoteChMinus.setTag(R.id.tag_keycode, AGKeyCode.CHANNEL_DOWN);
        imgBtnRemoteVolPlus.setTag(R.id.tag_keycode, AGKeyCode.VOLUME_UP);
        imgBtnRemoteVolMinus.setTag(R.id.tag_keycode, AGKeyCode.VOLUME_DOWN);
        btnRemote0.setTag(R.id.tag_keycode, AGKeyCode.NUM_0);
        btnRemote1.setTag(R.id.tag_keycode, AGKeyCode.NUM_1);
        btnRemote2.setTag(R.id.tag_keycode, AGKeyCode.NUM_2);
        btnRemote3.setTag(R.id.tag_keycode, AGKeyCode.NUM_3);
        btnRemote4.setTag(R.id.tag_keycode, AGKeyCode.NUM_4);
        btnRemote5.setTag(R.id.tag_keycode, AGKeyCode.NUM_5);
        btnRemote6.setTag(R.id.tag_keycode, AGKeyCode.NUM_6);
        btnRemote7.setTag(R.id.tag_keycode, AGKeyCode.NUM_7);
        btnRemote8.setTag(R.id.tag_keycode, AGKeyCode.NUM_8);
        btnRemote9.setTag(R.id.tag_keycode, AGKeyCode.NUM_9);


        setupListener();
    }

    private void setupListener() {
        imgBtnRemoteChPlus.setOnTouchListener(onMockKeyListener);
        imgBtnRemoteChMinus.setOnTouchListener(onMockKeyListener);
        imgBtnRemoteVolPlus.setOnTouchListener(onMockKeyListener);
        imgBtnRemoteVolMinus.setOnTouchListener(onMockKeyListener);
        btnRemote0.setOnTouchListener(onMockKeyListener);
        btnRemote1.setOnTouchListener(onMockKeyListener);
        btnRemote2.setOnTouchListener(onMockKeyListener);
        btnRemote3.setOnTouchListener(onMockKeyListener);
        btnRemote4.setOnTouchListener(onMockKeyListener);
        btnRemote5.setOnTouchListener(onMockKeyListener);
        btnRemote6.setOnTouchListener(onMockKeyListener);
        btnRemote7.setOnTouchListener(onMockKeyListener);
        btnRemote8.setOnTouchListener(onMockKeyListener);
        btnRemote9.setOnTouchListener(onMockKeyListener);


    }

    public class OnMockKeyListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int keyCode = (int) v.getTag(R.id.tag_keycode);
            final int action = event.getAction();
            L.d(TAG, "onTouch(), keyCode = " + keyCode + ", action = " + action);
            switch (action) {
                case ACTION_DOWN:
                    L.d(TAG, "onTouch(), key is pressed first");
                    if (appCookie.isConnect()) {
                        mPresenter.sendKeyCode(keyCode, (byte) action);
                    }
                    timerDisposable = Flowable.interval(500, 50, TimeUnit.MILLISECONDS)
                            .map(new Function<Long, Boolean>() {
                                @Override
                                public Boolean apply(@NonNull Long aLong) throws Exception {
                                    L.d(TAG, "onTouch(), key is pressed " + aLong.intValue());
                                    if (appCookie.isConnect()) {
                                        mPresenter.sendKeyCode(keyCode, (byte) action);
                                    }
                                    return true;
                                }
                            })
                            .subscribe();

                    break;

                case ACTION_MOVE:
                    break;

                case ACTION_UP:
                    if (timerDisposable != null && !timerDisposable.isDisposed()) {
                        timerDisposable.dispose();
                    }
                    mVibrator.vibrate(50);
                    if (appCookie.isConnect()) {
                        mPresenter.sendKeyCode(keyCode, (byte) action);
                    }
                    break;
            }


            return false;
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
