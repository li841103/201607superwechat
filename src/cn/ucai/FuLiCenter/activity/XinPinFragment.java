package cn.ucai.FuLiCenter.activity;


import android.content.Context;
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

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.XinPinAdapter;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class XinPinFragment extends Fragment {
    FuLiCenterActivity mContext;
    List<NewGoodBean> mlist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    XinPinAdapter mXinPinAdapter;
    GridLayoutManager mGridLayoutManager;
    TextView mtv_hint;
    int pageId=0;
    public XinPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuLiCenterActivity) getContext();
        View view = View.inflate(mContext, R.layout.fragment_xinpin, null);
        mlist = new ArrayList<NewGoodBean>();
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mtv_hint.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                pageId=0;
                initData();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int last;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==recyclerView.SCROLL_STATE_IDLE&&last==mXinPinAdapter.getItemCount()-1){
                    pageId+=I.PAGE_SIZE_DEFAULT;
                    initData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                last = mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initData() {
        final OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
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
                            mXinPinAdapter.initData(arr);
                        }

                    }

                    @Override
                    public void onError(String error) {
                      //  mtv_hint.setVisibility(View.GONE);
                       // mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initView(View view) {
        mtv_hint = (TextView) view.findViewById(R.id.refresh_hint);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_xinpin);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_xinpin);
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mXinPinAdapter = new XinPinAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mXinPinAdapter);
    }
    
    

}
