package cn.ucai.FuLiCenter.activity;

import android.os.Bundle;
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
    int index;
    int currentIndex;
    XinPinFragment mXinPinFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulicenter_main);
        initView();
        setRadioButtonStatus(index);
    }


    private void initView() {
        xinpin = (RadioButton) findViewById(R.id.xinpin);
        jingxuan = (RadioButton) findViewById(R.id.jingxuan);
        fenlei = (RadioButton) findViewById(R.id.fenlei);
        gouwuche = (RadioButton) findViewById(R.id.gouwuche);
        me = (RadioButton) findViewById(R.id.me);
        rbArr = new RadioButton[5];
        rbArr[0] = xinpin;
        rbArr[1] = jingxuan;
        rbArr[2] = fenlei;
        rbArr[3] = gouwuche;
        rbArr[4] = me;
        mXinPinFragment = new XinPinFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rela_layout, mXinPinFragment)
                .show(mXinPinFragment)
                .commit();
    }


    private void setRadioButtonStatus(int index) {
        for(int i=0;i<rbArr.length;i++){
            if(index!=currentIndex){
                rbArr[index].setChecked(true);
            }else {
                rbArr[index].setChecked(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
        if(index!=currentIndex){
            setRadioButtonStatus(index);
        }
    }
}
