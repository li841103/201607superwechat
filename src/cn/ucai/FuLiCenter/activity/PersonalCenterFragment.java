package cn.ucai.FuLiCenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.utils.UserUtils;

/**
 * Created by Zhou on 2016/8/8.
 */
public class PersonalCenterFragment extends Fragment{
    Context mContext;
    ImageView iv_msg,iv_avatar;
    TextView tv_sttings,tv_username,shoucangNum;
    RelativeLayout rl_soucang_baobei,rl_soucang_dianpu,rl_zhuji,My_dingdan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (FuLiCenterActivity) getContext();
        View layout = View.inflate(mContext, R.layout.personal_data, null);
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void initData() {
        if(DemoHXSDKHelper.getInstance().isLogined()){
            UserUtils.setAppCurrentUserNick(tv_username);
            UserUtils.setAppCurrentUserAvatar(mContext,iv_avatar);
        }
    }

    private void setListener() {
        setListenerclass listener = new setListenerclass();
        iv_avatar.setOnClickListener(listener);
        tv_sttings.setOnClickListener(listener);
        rl_soucang_baobei.setOnClickListener(listener);
        updateCollectCountListener();
    }

    private void initView(View layout) {
        shoucangNum = (TextView) layout.findViewById(R.id.shoucangNum);
        tv_sttings= (TextView) layout.findViewById(R.id.tv_sttings);
        iv_msg = (ImageView) layout.findViewById(R.id.iconfont_xinxi);
        iv_avatar = (ImageView) layout.findViewById(R.id.default_avatar);
        tv_username = (TextView) layout.findViewById(R.id.tv_username);
        rl_soucang_baobei = (RelativeLayout) layout.findViewById(R.id.rl_soucang_baobei);
        rl_soucang_dianpu = (RelativeLayout) layout.findViewById(R.id.rl_soucang_dianpu);
        rl_zhuji = (RelativeLayout) layout.findViewById(R.id.rl_zhuji);
        My_dingdan = (RelativeLayout) layout.findViewById(R.id.My_dingdan);
    }
    class setListenerclass implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(DemoHXSDKHelper.getInstance().isLogined()){
                switch (view.getId()){
                    case R.id.tv_sttings:
                    case R.id.default_avatar:
                        startActivity(new Intent(mContext, SettingsActivity.class));
                        break;
                    case R.id.rl_soucang_baobei:
                        startActivity(new Intent(mContext,CollectActivity.class));
                        break;
                }
            }
        }
    }

    class UpdateCollectCount extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int collectcount = FuLiCenterApplication.getInstance().getCollectcount();
            shoucangNum.setText(String.valueOf(collectcount));
        }
    }
    UpdateCollectCount mReceiver;
    private void updateCollectCountListener(){
        mReceiver = new UpdateCollectCount();
        IntentFilter filter = new IntentFilter("update_contact");
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }
}
