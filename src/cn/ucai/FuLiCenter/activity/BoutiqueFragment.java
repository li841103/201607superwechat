package cn.ucai.FuLiCenter.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.BoutiqueAdapter;
import cn.ucai.FuLiCenter.adapter.XinPinAdapter;
import cn.ucai.FuLiCenter.bean.BoutiqueBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    FuLiCenterActivity mContext;
    List<BoutiqueBean> mlist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    BoutiqueAdapter mBoutiqueAdapter;
    LinearLayoutManager mLinearLayoutManager;
    TextView mtv_hint;
    final static int PULL_DOWN = 1;
    final static int BUTTOM_DOWN = 0;
    boolean ismore = true;
    int first;


    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("main", "进入了Boutique的OnCreate方法");
        mContext = (FuLiCenterActivity) getContext();
        View view = inflater.inflate(R.layout.jingxuan_fragment, null,false);
        mlist = new ArrayList<BoutiqueBean>();
        initView(view);
        initData(BUTTOM_DOWN);
        setListener();
        return view;
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(first==0){
                    mtv_hint.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    ismore = true;
                    mBoutiqueAdapter.setFooter("没有更多数据可加载...");
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
                if(newState==recyclerView.SCROLL_STATE_IDLE&&last==mBoutiqueAdapter.getItemCount()-1&&ismore){
                    initData(PULL_DOWN);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = mLinearLayoutManager.findLastVisibleItemPosition();
                first=mLinearLayoutManager.findFirstVisibleItemPosition();
            }
        });
    }

    private void initData(final int DOWN_CODE) {
        final OkHttpUtils2<BoutiqueBean[]> utils = new OkHttpUtils2<BoutiqueBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<BoutiqueBean[]>() {
                    @Override
                    public void onSuccess(BoutiqueBean[] result) {
                        mtv_hint.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(result!=null){
                            Log.i("main", "BoutiqueBeanSize=" + result.length+"date="+ Arrays.toString(result));
                            ArrayList<BoutiqueBean> arr = Utils.array2List(result);
                            if(!ismore){
                                mBoutiqueAdapter.setFooter("没有更多数据可加载！");
                                return;
                            }
                            mBoutiqueAdapter.initData(arr,DOWN_CODE);
                            ismore = false;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        mtv_hint.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


    }

    private void initView(View view) {
        mtv_hint = (TextView) view.findViewById(R.id.refresh_hint_boutique);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_boutique);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_boutique);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBoutiqueAdapter = new BoutiqueAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mBoutiqueAdapter);
    }






}
