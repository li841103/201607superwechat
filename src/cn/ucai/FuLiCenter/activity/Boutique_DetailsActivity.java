package cn.ucai.FuLiCenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.XinPinAdapter;
import cn.ucai.FuLiCenter.bean.MessageBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.BackUtils;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class Boutique_DetailsActivity extends BaseActivity {
    Boutique_DetailsActivity mContext;
    List<NewGoodBean> mlist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    XinPinAdapter mXinPinAdapter;
    GridLayoutManager mGridLayoutManager;
    ImageView miv_soucang;
    TextView mtv_hint;
    int BoutiqueId = 0;
    String title;
    final static int PULL_DOWN = 1;
    final static int BUTTOM_DOWN = 0;
    int pageId=0;
    boolean ismore = true;
    int first;


    public Boutique_DetailsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        setContentView(R.layout.fragment_boutique);
        mlist = new ArrayList<NewGoodBean>();
        initView();
        initData(BUTTOM_DOWN);
        setListener();
        initDetailsActivity();
    }

    private void initDetailsActivity() {
        if(DemoHXSDKHelper.getInstance().isLogined()){
            OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
            utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.GOODS_ID,String.valueOf(BoutiqueId))
                    .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                miv_soucang.setImageResource(R.drawable.bg_collect_out);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }


    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(first==0){
                    mtv_hint.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    ismore = true;
                    mXinPinAdapter.setFooter("加载更多数据...");
                    pageId=0;
                    initData(BUTTOM_DOWN);
                }
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int last;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("main", "recyclerView.SCROLL_STATE_IDLE=" + newState + "   last==" + last + "  ismore=" + ismore+"    First="+first);
                if(newState==recyclerView.SCROLL_STATE_IDLE&&last==mXinPinAdapter.getItemCount()-1&&ismore){
                    pageId+=I.PAGE_SIZE_DEFAULT;
                    initData(PULL_DOWN);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = mGridLayoutManager.findLastVisibleItemPosition();
                first=mGridLayoutManager.findFirstVisibleItemPosition();
            }
        });
    }

    private void initData(final int DOWN_CODE) {
        BoutiqueId = getIntent().getIntExtra(D.Boutique.KEY_ID, 0);

        final OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(BoutiqueId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
                    @Override
                    public void onSuccess(NewGoodBean[] result) {
                        mtv_hint.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(result!=null){
                            Log.i("main", "newchangdu=" + result.length);
                            ArrayList<NewGoodBean> arr = Utils.array2List(result);
                            if(arr.size()<I.PAGE_SIZE_DEFAULT){
                                mXinPinAdapter.setFooter("没有更多数据可加载！");
                                ismore = false;
                            }
                            mXinPinAdapter.initData(arr,DOWN_CODE);
                        }

                    }

                    @Override
                    public void onError(String error) {
                      //  mtv_hint.setVisibility(View.GONE);
                       // mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initView() {
        miv_soucang = (ImageView) findViewById(R.id.shangpin_title_collect);
        title=getIntent().getStringExtra(D.Boutique.KEY_NAME);
        BackUtils.ActivityBack(this,title);
        mtv_hint = (TextView)findViewById(R.id.boutique_refresh_hint);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.boutique_srl_xinpin);
        mRecyclerView = (RecyclerView)findViewById(R.id.boutique_rl_xinpin);
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mXinPinAdapter = new XinPinAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mXinPinAdapter);
    }



    
    

}
