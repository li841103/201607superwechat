package cn.ucai.FuLiCenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.CollectAdapter;
import cn.ucai.FuLiCenter.adapter.XinPinAdapter;
import cn.ucai.FuLiCenter.bean.CollectBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.BackUtils;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectActivity extends BaseActivity {
    CollectActivity mContext;
    List<CollectBean> mlist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    CollectAdapter mcollectAdapter;
    GridLayoutManager mGridLayoutManager;
    TextView mtv_hint;
    final static int PULL_DOWN = 1;
    final static int BUTTOM_DOWN = 0;
    int pageId=0;
    boolean ismore = true;
    int first;


    public CollectActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Log.i("main", "进入了Collect的OnCreate方法");
        mContext = this;
        setContentView(R.layout.fragment_collect);
        mlist = new ArrayList<CollectBean>();
        initView();
        initData(BUTTOM_DOWN);
        setListener();
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(first==0){
                    mtv_hint.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    ismore = true;
                    mcollectAdapter.setFooter("加载更多数据...");
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
                if(newState==recyclerView.SCROLL_STATE_IDLE&&last==mcollectAdapter.getItemCount()-1&&ismore){
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

        final OkHttpUtils2<CollectBean[]> utils = new OkHttpUtils2<CollectBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        mtv_hint.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(result!=null){
                            Log.i("main", "collect长度：=" + result.length);
                            ArrayList<CollectBean> arr = Utils.array2List(result);
                            if(arr.size()<I.PAGE_SIZE_DEFAULT){
                                mcollectAdapter.setFooter("没有更多数据可加载！");
                                ismore = false;
                            }
                            mcollectAdapter.initData(arr,DOWN_CODE);
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
        mtv_hint = (TextView)findViewById(R.id.refresh_hint);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_xinpin);
        mRecyclerView = (RecyclerView)findViewById(R.id.rl_xinpin);
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mcollectAdapter = new CollectAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mcollectAdapter);
    }



    
    

}
