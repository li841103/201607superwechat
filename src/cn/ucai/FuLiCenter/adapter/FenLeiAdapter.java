package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.util.Log;
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
import cn.ucai.FuLiCenter.utils.ImageUtils;

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
    public CategoryGroupBean getGroup(int i) {
        return mGroupList.get(i);
    }

    @Override
    public CategoryChildBean getChild(int i, int i1) {
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
    public View getChildView(int groupPosition, int sonPosition, boolean b, View view, ViewGroup viewGroup) {
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
        CategoryChildBean child = (CategoryChildBean) getChild(groupPosition, sonPosition);//得到当前这个具体的子类的对象
        if(child!=null){//如果拿到了这个数据
            ImageUtils.setFenLeiSonImage(mContext,child.getImageUrl(),holder.mivSonFindNormal);//设置子类图片控件显示当前子类对象的图片
            holder.mtvSonName.setText(child.getName());//设置子类控件显示当前子类对象的名称
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void addAll(List<CategoryGroupBean> mCategoryGroupBean, List<ArrayList<CategoryChildBean>> mCategoryChildBean) {
        this.mGroupList.clear();
        this.mGroupList.addAll(mCategoryGroupBean);
        this.mSonList.clear();
        this.mSonList.addAll(mCategoryChildBean);
        notifyDataSetChanged();
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
