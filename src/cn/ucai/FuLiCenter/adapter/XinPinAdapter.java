package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.activity.shangpinxiangqingActivity;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.ImageUtils;
import cn.ucai.FuLiCenter.widget.I;


/**
 * Created by Zhou on 2016/8/1.
 */
public class XinPinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<NewGoodBean> mList;
    XinPinViewHolder mXinPinViewHolder;
    FootViewHolder mFootViewHolder;
    String footer;


    public void setFooter(String footer) {
        this.footer = footer;
    }

    public XinPinAdapter(Context mcontext, List<NewGoodBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<NewGoodBean>();
        this.mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(viewType==I.TYPE_FOOTER){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.foot_item, null, false);
            holder = new FootViewHolder(view);
        }
        if(viewType==I.TYPE_ITEM){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.xinpin_item, null, false);
            holder = new XinPinViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else{
            return I.TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("main", "xinpingonBindViewHolder");
        if(holder instanceof XinPinViewHolder){
           final NewGoodBean xin = mList.get(position);
            mXinPinViewHolder = (XinPinViewHolder) holder;
            mXinPinViewHolder.mmtvMoney.setText(xin.getCurrencyPrice());
            mXinPinViewHolder.mmtvDesc.setText(xin.getGoodsBrief());
            ImageUtils.setXinPinImage(mcontext,xin.getGoodsThumb(),mXinPinViewHolder.mmivImage);
            mXinPinViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcontext.startActivity(new Intent(mcontext, shangpinxiangqingActivity.class).putExtra(D.GoodDetails.KEY_GOODS_ID, xin.getGoodsId()));
                }
            });
        }
        if(holder instanceof FootViewHolder){
            mFootViewHolder = (FootViewHolder) holder;
            mFootViewHolder.mfoothint.setText(footer);
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public void initData(ArrayList<NewGoodBean> arr,int DOWN_CODE) {
        if(mList!=null&&DOWN_CODE==0){
            Log.i("main", "mList!=null");
            mList.clear();
            mList.addAll(arr);
            soryByAddTime();
            notifyDataSetChanged();
        }
        if(mList!=null&&DOWN_CODE==1){
            mList.addAll(arr);
            soryByAddTime();
            notifyDataSetChanged();
        }
    }


    class XinPinViewHolder extends RecyclerView.ViewHolder{
        ImageView mmivImage;
        TextView mmtvDesc,mmtvMoney;
        LinearLayout layout;
        public XinPinViewHolder(View itemView) {
            super(itemView);
            mmtvDesc= (TextView) itemView.findViewById(R.id.tvDesc);
            mmivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            mmtvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_xinpin);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder{
        TextView mfoothint;

        public FootViewHolder(View itemView) {
            super(itemView);
            mfoothint = (TextView) itemView.findViewById(R.id.foot_hint);

        }
    }

    private void soryByAddTime(){
        Collections.sort(mList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean left, NewGoodBean right) {
                return (int) (left.getAddTime()-right.getAddTime());
            }
        });
    }



}
