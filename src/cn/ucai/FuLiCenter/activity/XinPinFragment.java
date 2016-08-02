package cn.ucai.FuLiCenter.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.XinPinAdapter;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class XinPinFragment extends Fragment {
    Context mContext;
    List<NewGoodBean> mlist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    XinPinAdapter mXinPinAdapter;
    GridLayoutManager mGridLayoutManager;
    public XinPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_xinpin, null);
        mlist = new ArrayList<NewGoodBean>();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_xinpin);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_xinpin);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mXinPinAdapter = new XinPinAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mXinPinAdapter);
    }

}
