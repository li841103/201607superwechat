package cn.ucai.FuLiCenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.Arrays;
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
    int groupCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (FuLiCenterActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fenlei_fragment, null);
        mCategoryGroupBean = new ArrayList<CategoryGroupBean>();
        mCategoryChildBean = new ArrayList<ArrayList<CategoryChildBean>>();
        mFenLeiAdapter = new FenLeiAdapter(mContext,mCategoryGroupBean,mCategoryChildBean);
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        DownGroupData(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null){
                    Log.i("main", "大类数据下载完成,大类数据为："+ Arrays.toString(result));
                    ArrayList<CategoryGroupBean> groupList = Utils.array2List(result);
                    mCategoryGroupBean = groupList;
                    int i=0;
                    for(CategoryGroupBean g:groupList){
                        mCategoryChildBean.add(new ArrayList<CategoryChildBean>());
                        DownSonData(i,g.getId());
                        i++;
                    }

                }
            }

            @Override
            public void onError(String error) {
                Log.i("main", "error=" + error);
            }
        });
    }
    private void DownSonData(final int i,int id){
        OkHttpUtils2<CategoryChildBean[]> utils = new OkHttpUtils2<CategoryChildBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, String.valueOf(id))//需要大类的ID
                .addParam(I.PAGE_ID, String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        groupCount++;
                        if(result!=null){
                            Log.i("main", "小类数据下载成功！i="+groupCount);
                                ArrayList<CategoryChildBean> categoryChild = Utils.array2List(result);
                            if(categoryChild!=null){
                                mCategoryChildBean.set(i, categoryChild);
                            }
                            if(groupCount==mCategoryGroupBean.size()){
                                Log.i("main", "去执行了addall方法");
                                mFenLeiAdapter.addAll(mCategoryGroupBean,mCategoryChildBean);
                            }
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.i("main", "小类数据下载不成功！");
                    }
                });
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
        mExpandableListView.setAdapter(mFenLeiAdapter);
    }
}
