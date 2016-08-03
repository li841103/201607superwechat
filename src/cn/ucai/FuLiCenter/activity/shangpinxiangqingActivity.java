package cn.ucai.FuLiCenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.GoodDetails;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.view.FlowIndicator;
import cn.ucai.FuLiCenter.view.SlideAutoLoopView;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/8/3.
 */
public class shangpinxiangqingActivity extends BaseActivity {
    Context mContext;
    TextView mEnglish,mChese,mMoney;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.shangpin_xiangqing);
        mContext = this;
        initView();
        initData();
    }

    private void initData() {
        int intExtra = getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID, 0);
        OkHttpUtils2<GoodDetails> utils = new OkHttpUtils2<GoodDetails>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(intExtra))
                .targetClass(GoodDetails.class)
                .execute(new OkHttpUtils2.OnCompleteListener<GoodDetails>() {
                    @Override
                    public void onSuccess(GoodDetails result) {
                        if(result!=null){
                            setGoodDetails(result);
                        }else{
                            Toast.makeText(mContext, "获取数据失败,数据集为空！", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

    }

    private void setGoodDetails(GoodDetails result) {
        mEnglish.setText(result.getGoodsEnglishName());
        mChese.setText(result.getGoodsName());
        mMoney.setText(result.getPromotePrice());
    }

    private void initView() {
        mEnglish = (TextView) findViewById(R.id.english_name);
        mChese = (TextView) findViewById(R.id.chese_name);
        mMoney = (TextView) findViewById(R.id.money);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
    }
}
