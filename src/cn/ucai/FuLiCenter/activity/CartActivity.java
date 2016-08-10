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
public class CartActivity extends BaseActivity {
    CartActivity mContext;
    ArrayList<CartBean> mlist= (ArrayList<CartBean>) FuLiCenterApplication.getInstance().getCartBeanList();
    RecyclerView mRecyclerView;
    CartAdapter mCartAdapter;
    LinearLayoutManager mLinearLayoutManager;
    TextView mtv_Total,mtv_Save,mtv_Num;
    Button mbtn_Purchase;
    Button mtn_reduce,mtn_add;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.cart_fragment);
        mContext=this;
        initView();
        initData();
    }



    private void initData() {
        mCartAdapter.initData(mlist,0);
    }




    private void initView() {
        mtv_Num = (TextView) findViewById(R.id.tv_num);
        mtn_reduce = (Button) findViewById(R.id.tv_reduce);
        mtn_add = (Button) findViewById(R.id.tv_add);
        mtv_Total = (TextView) findViewById(R.id.tv_Total);
        mtv_Save = (TextView) findViewById(R.id.tv_save);
        mbtn_Purchase = (Button) findViewById(R.id.btn_purchase);
        mRecyclerView = (RecyclerView) findViewById(R.id.cart_rlv);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCartAdapter = new CartAdapter(mContext, mlist);
        mRecyclerView.setAdapter(mCartAdapter);

    }






}
