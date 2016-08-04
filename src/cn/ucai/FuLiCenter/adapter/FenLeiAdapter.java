package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.CategoryChildBean;
import cn.ucai.FuLiCenter.bean.CategoryGroupBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;

/**
 * Created by Zhou on 2016/8/4.
 */
public class FenLeiAdapter extends BaseExpandableListAdapter{

    Context mContext;
    List<CategoryGroupBean> mGroupList;  //大类的集合
    List<ArrayList<CategoryChildBean>>  mSonList; //小类的集合

    public FenLeiAdapter(Context mcontext, List<CategoryGroupBean> mGroupList, List<ArrayList<CategoryChildBean>> mSonList) {
        this.mContext = mcontext;
        this.mGroupList = new ArrayList<CategoryGroupBean>();
        this.mGroupList.addAll(mGroupList);
        this.mSonList = new ArrayList<ArrayList<CategoryChildBean>>();
        this.mSonList.addAll(mSonList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList==null?0:mGroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mSonList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mSonList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;

    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder=null;
        if(view==null){//等于Null 说明是第一次创建
            view = View.inflate(mContext, R.layout.fenlei_group, null);
            holder = new GroupViewHolder();
            holder.mivGroupFindNormal = (ImageView) view.findViewById(R.id.iv_group_find_normal);
            holder.mtvGroupName = (TextView) view.findViewById(R.id.tv_group_name);
            holder.mivGroupExpand = (ImageView) view.findViewById(R.id.iv_group_expand);
            view.setTag(holder);
        }else{
            holder = (GroupViewHolder) view.getTag();
        }
        if(b){//布尔b表示这个大类别有没有被打开
            holder.mivGroupExpand.setImageResource(R.drawable.expand_off);
        }else{
            holder.mivGroupExpand.setImageResource(R.drawable.expand_on);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        SonViewHolder holder = null;
        if(view==null){//第一次创建
            holder = new SonViewHolder();
            view = View.inflate(mContext,R.layout.fenlei_son,null);
            holder.mivSonFindNormal= (ImageView) view.findViewById(R.id.iv_son_find_normal);
            holder.mtvSonName = (TextView) view.findViewById(R.id.tv_son_name);
            view.setTag(holder);
        }else{                      //不是第一次创建时   直接从存在view的tag中取得
            holder = (SonViewHolder) view.getTag();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder{
        ImageView mivGroupFindNormal;
        TextView mtvGroupName;
        ImageView mivGroupExpand;
    }

    class SonViewHolder{
        ImageView mivSonFindNormal;
        TextView mtvSonName;
    }
}
