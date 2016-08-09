package cn.ucai.FuLiCenter.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.CartAdapter;
import cn.ucai.FuLiCenter.adapter.CartAdapter;
import cn.ucai.FuLiCenter.bean.CartBean;
import cn.ucai.FuLiCenter.bean.CartBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    FuLiCenterActivity mContext;
    ArrayList<CartBean> mlist= (ArrayList<CartBean>) FuLiCenterApplication.getInstance().getCartBeanList();
    RecyclerView mRecyclerView;
    CartAdapter mCartAdapter;
    LinearLayoutManager mLinearLayoutManager;
    TextView mtv_Total,mtv_Save;
    Button mbtn_Purchase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, null, false);
        mContext=(FuLiCenterActivity)getContext();
        initView(view);
        initData();
        return view;
}

    private void initData() {
        mCartAdapter.initData(mlist,0);
    }

    public CartFragment() {
        // Required empty public constructor
    }



    private void initView(View view) {
        mtv_Total = (TextView) view.findViewById(R.id.tv_Total);
        mtv_Save = (TextView) view.findViewById(R.id.tv_save);
        mbtn_Purchase = (Button) view.findViewById(R.id.btn_purchase);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cart_rlv);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCartAdapter = new CartAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mCartAdapter);
    }






}
