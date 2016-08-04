package cn.ucai.FuLiCenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.adapter.FenLeiAdapter;
import cn.ucai.FuLiCenter.bean.CategoryChildBean;
import cn.ucai.FuLiCenter.bean.CategoryGroupBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/8/4.
 */
public class FenLeiFragment extends Fragment{
    Context mContext;
    ExpandableListView mExpandableListView;
    List<CategoryGroupBean> mCategoryGroupBean;
    List<ArrayList<CategoryChildBean>> mCategoryChildBean;
    FenLeiAdapter mFenLeiAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (FuLiCenterActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fenlei_fragment, null);
        mCategoryGroupBean = new ArrayList<CategoryGroupBean>();
        mCategoryChildBean = new ArrayList<ArrayList<CategoryChildBean>>();
        initView(layout);
        initData();
    }

    private void initData() {
        DownGroupData(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null){
                    ArrayList<CategoryGroupBean> groupList = Utils.array2List(result);
                    for(CategoryGroupBean g:groupList){
                        DownSonData(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                            @Override
                            public void onSuccess(CategoryChildBean[] result) {
                                    if (result!=null){
                                        ArrayList<CategoryChildBean> categoryChildList = Utils.array2List(result);

                                    }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        },g.getId());
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.i("main", "error=" + error);
            }
        });
    }
    private void DownSonData(OkHttpUtils2.OnCompleteListener<CategoryChildBean[]> listener,int id){
        OkHttpUtils2<CategoryChildBean[]> utils = new OkHttpUtils2<CategoryChildBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, String.valueOf(id))//需要大类的ID
                .addParam(I.PAGE_ID, String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
    private void DownGroupData(OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils2<CategoryGroupBean[]> utils = new OkHttpUtils2<CategoryGroupBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)//下载分类首页的信息
            .targetClass(CategoryGroupBean[].class)
                .execute(listener);

    }
    //Context mcontext, List<CategoryGroupBean> mGroupList, List<ArrayList<CategoryChildBean>> mSonList
    private void initView(View layout) {
        mExpandableListView = (ExpandableListView) layout.findViewById(R.id.fenlei_elv);
        mExpandableListView.setGroupIndicator(null);
        mFenLeiAdapter = new FenLeiAdapter(mContext,mCategoryGroupBean,mCategoryChildBean);
        mExpandableListView.setAdapter(mFenLeiAdapter);

    }
}
