package cn.ucai.FuLiCenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.utils.Utils;

/**
 * Created by Zhou on 2016/8/1.
 */
public class FuLiCenterActivity extends BaseActivity implements View.OnClickListener{
    RadioButton xinpin,jingxuan,fenlei,gouwuche,me;
    RadioButton[] rbArr;
    Fragment[] fragment;
    TextView mtvHint;
    int index;
    int currentIndex;
    XinPinFragment mXinPinFragment;
    BoutiqueFragment mBoutiqueFragment;
    FenLeiFragment mFenLeiFragment;
    PersonalCenterFragment mPersonalCenterFragment;
    CartFragment mCartFragment;
    final static int LOGIN_CODE = 100;
    updateCartNumReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulicenter_main);
        initView();
       // setRadioButtonStatus(index);
        setListener();
    }

    private void setListener() {
        setUpdateCartListener();
        xinpin.setOnClickListener(this);
        jingxuan.setOnClickListener(this);
        fenlei.setOnClickListener(this);
        gouwuche.setOnClickListener(this);
        me.setOnClickListener(this);

    }


    private void initView() {
        mtvHint = (TextView) findViewById(R.id.tvCartHint);
        xinpin = (RadioButton) findViewById(R.id.xinpin);
        jingxuan = (RadioButton) findViewById(R.id.jingxuan);
        fenlei = (RadioButton) findViewById(R.id.fenlei);
        gouwuche = (RadioButton) findViewById(R.id.gouwuche);
        me = (RadioButton) findViewById(R.id.me);
        mXinPinFragment = new XinPinFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mFenLeiFragment = new FenLeiFragment();
        mPersonalCenterFragment = new PersonalCenterFragment();
        mCartFragment = new CartFragment();
        rbArr = new RadioButton[5];
        rbArr[0] = xinpin;
        rbArr[1] = jingxuan;
        rbArr[2] = fenlei;
        rbArr[3] = gouwuche;
        rbArr[4] = me;
        fragment = new Fragment[5];
        fragment[0] = mXinPinFragment;
        fragment[1] = mBoutiqueFragment;
        fragment[2] = mFenLeiFragment;
        fragment[3] = mCartFragment;
        fragment[4] = mPersonalCenterFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rela_layout,mXinPinFragment)
                .add(R.id.rela_layout, mBoutiqueFragment)
                .add(R.id.rela_layout,mFenLeiFragment)
                .hide(mBoutiqueFragment).hide(mFenLeiFragment)
                .show(mXinPinFragment)
                .commit();
        xinpin.setChecked(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xinpin:
                index = 0;
                break;
            case R.id.jingxuan:
                index = 1;
                break;
            case R.id.fenlei:
                index = 2;
                break;
            case R.id.gouwuche:
                index = 3;
                break;
            case R.id.me:
                if(DemoHXSDKHelper.getInstance().isLogined()){
                    index = 4;
                }else{
                    gotoLogin();
                }
                break;
        }
        rbArr[index].setChecked(true);
        rbArr[currentIndex].setChecked(false);
        setShowFragment();
    }

    private void gotoLogin() {
        startActivityForResult(new Intent(this,LoginActivity.class),LOGIN_CODE);
    }



     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOGIN_CODE){
            if(DemoHXSDKHelper.getInstance().isLogined()){
                index = 4;
            }else{
                index = currentIndex;
                setShowFragment();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoHXSDKHelper.getInstance().isLogined()) {

        }else{
            if(index==4){
                index = 0;
                setShowFragment();
            }
        }
    }

    private void setShowFragment() {
        FragmentTransaction trx =getSupportFragmentManager().beginTransaction();
        if (index != currentIndex) {
            if(!fragment[index].isAdded()){
                trx.add(R.id.rela_layout, fragment[index]);
            }
            trx.hide(fragment[currentIndex]).show(fragment[index]).commit();
            Log.i("main", "index=" + index+"    currentIndex="+currentIndex);
        }
        rbArr[index].setChecked(true);
        rbArr[currentIndex].setChecked(false);
        currentIndex = index;
    }

    class updateCartNumReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartNum();
        }
    }
    private void setUpdateCartListener(){
        mReceiver = new updateCartNumReceiver();
        IntentFilter intentFilter = new IntentFilter("update_cart_list");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }

    private void updateCartNum() {
        int count = Utils.sumCartCount();
        if(!DemoHXSDKHelper.getInstance().isLogined()||count==0) {
            mtvHint.setText(String.valueOf(0));
            mtvHint.setVisibility(View.GONE);
        }else{
            mtvHint.setText(String.valueOf(count));
            mtvHint.setVisibility(View.VISIBLE);
        }
    }
}

