package cn.ucai.FuLiCenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.AlbumsBean;
import cn.ucai.FuLiCenter.bean.GoodDetails;
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
    GoodDetails mGoodDetails;
    WebView mWebView;
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
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(intExtra))
                .targetClass(GoodDetails.class)
                .execute(new OkHttpUtils2.OnCompleteListener<GoodDetails>() {
                    @Override
                    public void onSuccess(GoodDetails result) {
                        if(result!=null){
                            mGoodDetails = result;
                            setGoodDetails();
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

    private void setGoodDetails() {
        mEnglish.setText(mGoodDetails.getGoodsEnglishName());
        mChese.setText(mGoodDetails.getGoodsName());
        mMoney.setText(mGoodDetails.getPromotePrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator, getAlbumImageUrl(), getAlbumImageSize());
        mWebView.loadDataWithBaseURL(null,mGoodDetails.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);
    }

    private int getAlbumImageSize() {
        if(mGoodDetails.getProperties()!=null&&mGoodDetails.getProperties().length>0){
            return mGoodDetails.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImageUrl() {
        String[] albumImageUrl = new String[]{};
        if(mGoodDetails.getProperties()!=null&&mGoodDetails.getProperties().length>0){
            AlbumsBean[] albums = mGoodDetails.getProperties()[0].getAlbums();
            albumImageUrl = new String[albums.length];
            for(int i=0;i<albumImageUrl.length;i++){
                albumImageUrl[i] = albums[i].getImgUrl();
            }
        }
        return albumImageUrl;
    }

    private void initView() {
        mEnglish = (TextView) findViewById(R.id.english_name);
        mChese = (TextView) findViewById(R.id.chese_name);
        mMoney = (TextView) findViewById(R.id.money);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        mWebView = (WebView) findViewById(R.id.web_view);
    }
}
