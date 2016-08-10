package cn.ucai.FuLiCenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.AlbumsBean;
import cn.ucai.FuLiCenter.bean.GoodDetailsBean;
import cn.ucai.FuLiCenter.bean.MessageBean;
import cn.ucai.FuLiCenter.task.DownCollectCountTask;
import cn.ucai.FuLiCenter.utils.BackUtils;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.view.FlowIndicator;
import cn.ucai.FuLiCenter.view.SlideAutoLoopView;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/8/3.
 */
public class shangpinxiangqingActivity extends BaseActivity {
    Context mContext;
    TextView mEnglish,mChese,mMoney,mtvNum;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    GoodDetailsBean mGoodDetails;
    RelativeLayout mrlcart;
    WebView mWebView;
    int intExtra;
    boolean isSouCang;
    ImageView miv_soucang,miv_share;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.shangpin_xiangqing);
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        miv_soucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DemoHXSDKHelper.getInstance().isLogined()){
                    initDetailsActivity();
                    if(isSouCang){//为true时是收藏状态
                        isSouCang = false;
                        ImageCode(isSouCang);//将图片现改为未收藏状态
                        //然后删除数据库中的收藏商品
                        deleteSouCang();
                    }else{//未收藏状态添加收藏
                        addSouCang();
                    }
                }else{
                    //还没有登录
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
        miv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
        mrlcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mGoodDetails.getShareUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mGoodDetails.getGoodsName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    public void addSouCang(){
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                        .addParam(I.Collect.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                        .addParam(I.Collect.GOODS_ID,String.valueOf(mGoodDetails.getGoodsId()))
                        .addParam(I.Collect.GOODS_NAME,mGoodDetails.getGoodsName())
                        .addParam(I.Collect.GOODS_ENGLISH_NAME,mGoodDetails.getGoodsEnglishName())
                        .addParam(I.Collect.GOODS_THUMB,mGoodDetails.getGoodsThumb())
                        .addParam(I.Collect.GOODS_IMG,mGoodDetails.getGoodsImg())
                        .addParam(I.Collect.ADD_TIME,String.valueOf(mGoodDetails.getAddTime()))
                        .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            isSouCang = true;
                            ImageCode(isSouCang);
                            new DownCollectCountTask(mContext).exec(FuLiCenterApplication.getInstance().getUserName());
                            Toast.makeText(mContext, "添加收藏成功！", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(mContext, "添加收藏失败！", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.i("main", "error=" + error);
                    }
                });


    }
    public void deleteSouCang(){
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID, String.valueOf(mGoodDetails.getGoodsId()))
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            new DownCollectCountTask(mContext).exec(FuLiCenterApplication.getInstance().getUserName());
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(mContext,"删除失败",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(String error) {

                        Toast.makeText(mContext,error,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initDetailsActivity() {
        if(DemoHXSDKHelper.getInstance().isLogined()){
            OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
            utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.GOODS_ID,String.valueOf(intExtra))
                    .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                Log.i("main", "result=" + result.toString());
                                isSouCang = true;
                                miv_soucang.setImageResource(R.drawable.bg_collect_out);
                            }else{
                                isSouCang = false;
                                miv_soucang.setImageResource(R.drawable.bg_collect_in);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }else{
            startActivity(new Intent(this, LoginActivity.class));
            Log.i("main", "检测到并没有登录！！@");
        }
    }

    public void ImageCode(boolean isCheck){
        if(isCheck){
            miv_soucang.setImageResource(R.drawable.bg_collect_out);
        }else{
            miv_soucang.setImageResource(R.drawable.bg_collect_in);
        }
    }


    private void initData() {
        mtvNum.setText(String.valueOf(Utils.sumCartCount()));
        BackUtils.ActivityBack(shangpinxiangqingActivity.this);
        intExtra = getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID, 0);
        OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(intExtra))
                .targetClass(GoodDetailsBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                    @Override
                    public void onSuccess(GoodDetailsBean result) {
                        if(result!=null){
                            if(DemoHXSDKHelper.getInstance().isLogined()){
                                initDetailsActivity();
                            }else{
                                ImageCode(false);
                            }
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
        mrlcart = (RelativeLayout) findViewById(R.id.rl_cart);
        mtvNum = (TextView) findViewById(R.id.tv_num);
        miv_share = (ImageView) findViewById(R.id.shangpin_title_share_nor);
        miv_soucang = (ImageView) findViewById(R.id.shangpin_title_collect);
        mEnglish = (TextView) findViewById(R.id.english_name);
        mChese = (TextView) findViewById(R.id.chese_name);
        mMoney = (TextView) findViewById(R.id.money);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        mWebView = (WebView) findViewById(R.id.web_view);
    }
}
