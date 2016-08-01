package cn.ucai.FuLiCenter.activity;

import android.os.Bundle;
import android.widget.RadioButton;

import cn.ucai.FuLiCenter.R;

/**
 * Created by Zhou on 2016/8/1.
 */
public class FuLiCenterActivity extends BaseActivity {
    RadioButton xinpin,jingxuan,fenlei,gouwuche,shoucang,me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulicenter_main);
        initView();
    }

    private void initView() {
        xinpin = (RadioButton) findViewById(R.id.xinpin);
        jingxuan = (RadioButton) findViewById(R.id.xinpin);
        fenlei = (RadioButton) findViewById(R.id.xinpin);
        gouwuche = (RadioButton) findViewById(R.id.xinpin);
        shoucang = (RadioButton) findViewById(R.id.xinpin);
        me = (RadioButton) findViewById(R.id.xinpin);
        RadioButton[] rbArr = new RadioButton[5];
        //rbArr[0]=
    }
}
