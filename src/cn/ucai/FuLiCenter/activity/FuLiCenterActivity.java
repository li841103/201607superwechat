package cn.ucai.FuLiCenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.ucai.FuLiCenter.R;

/**
 * Created by Zhou on 2016/8/1.
 */
public class FuLiCenterActivity extends BaseActivity implements View.OnClickListener{
    RadioButton xinpin,jingxuan,fenlei,gouwuche,me;
    RadioButton[] rbArr;
    Fragment[] fragment;
    int index;
    int currentIndex;
    XinPinFragment mXinPinFragment;
    BoutiqueFragment mBoutiqueFragment;
    FenLeiFragment mFenLeiFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulicenter_main);
        initView();
       // setRadioButtonStatus(index);
        setListener();
    }

    private void setListener() {
        xinpin.setOnClickListener(this);
        jingxuan.setOnClickListener(this);
        fenlei.setOnClickListener(this);
        gouwuche.setOnClickListener(this);
        me.setOnClickListener(this);
    }


    private void initView() {
        xinpin = (RadioButton) findViewById(R.id.xinpin);
        jingxuan = (RadioButton) findViewById(R.id.jingxuan);
        fenlei = (RadioButton) findViewById(R.id.fenlei);
        gouwuche = (RadioButton) findViewById(R.id.gouwuche);
        me = (RadioButton) findViewById(R.id.me);
        mXinPinFragment = new XinPinFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mFenLeiFragment = new FenLeiFragment();
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
                index = 4;
                break;
        }
        rbArr[index].setChecked(true);
        rbArr[currentIndex].setChecked(false);
        FragmentTransaction trx =getSupportFragmentManager().beginTransaction();
        if (index != currentIndex) {
            if(!fragment[index].isAdded()){
                trx.add(R.id.rela_layout, fragment[index]);
            }
            trx.hide(fragment[currentIndex]).show(fragment[index]).commit();
            Log.i("main", "index=" + index);
//            trx.show(fragment[index]).commit();
            currentIndex = index;
        }
    }
}

