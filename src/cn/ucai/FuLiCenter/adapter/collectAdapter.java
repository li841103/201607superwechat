package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.activity.shangpinxiangqingActivity;
import cn.ucai.FuLiCenter.bean.CollectBean;
import cn.ucai.FuLiCenter.bean.MessageBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.task.DownCollectCountTask;
import cn.ucai.FuLiCenter.utils.ImageUtils;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.widget.I;


/**
 * Created by Zhou on 2016/8/1.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<CollectBean> mList;
    CollectViewHolder mCollectViewHolder;
    FootViewHolder mFootViewHolder;
    String footer;


    public void setFooter(String footer) {
        this.footer = footer;
    }

    public CollectAdapter(Context mcontext, List<CollectBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<CollectBean>();
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
            View view = LayoutInflater.from(mcontext).inflate(R.layout.collect_item, null, false);
            holder = new CollectViewHolder(view);
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

        if(holder instanceof CollectViewHolder){
            final CollectBean collect = mList.get(position);
            mCollectViewHolder = (CollectViewHolder) holder;
            mCollectViewHolder.mmtvDesc.setText(collect.getGoodsName());
            ImageUtils.setXinPinImage(mcontext,collect.getGoodsThumb(),mCollectViewHolder.mmivImage);
            mCollectViewHolder.mrl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcontext.startActivity(new Intent(mcontext, shangpinxiangqingActivity.class).putExtra(D.GoodDetails.KEY_GOODS_ID,collect.getGoodsId()));
                }
            });
            mCollectViewHolder.mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
                    utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                            .addParam(I.Collect.GOODS_ID, String.valueOf(collect.getGoodsId()))
                            .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                            .targetClass(MessageBean.class)
                            .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if(result!=null&&result.isSuccess()){
                                        mList.remove(collect);
                                        notifyDataSetChanged();
                                        new DownCollectCountTask().exec(FuLiCenterApplication.getInstance().getUserName());
                                        Toast.makeText(mcontext,"删除成功",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(mcontext,"删除失败",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                    Toast.makeText(mcontext,error,Toast.LENGTH_LONG).show();
                                }
                            });
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
        return mList==null?0:mList.size()+1;
    }

    public void initData(ArrayList<CollectBean> arr,int DOWN_CODE) {
        if(mList!=null&&DOWN_CODE==0){
            Log.i("main", "mList!=null");
            mList.clear();
            mList.addAll(arr);
            notifyDataSetChanged();
        }
        if(mList!=null&&DOWN_CODE==1){
            mList.addAll(arr);
            notifyDataSetChanged();
        }
    }


    class CollectViewHolder extends RecyclerView.ViewHolder{
        ImageView mmivImage,mdelete;
        TextView mmtvDesc;
        RelativeLayout mrl_layout;
        public CollectViewHolder(View itemView) {
            super(itemView);
            mrl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout_shoucang);
            mmtvDesc= (TextView) itemView.findViewById(R.id.tvDesc);
            mmivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            mdelete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder{
        TextView mfoothint;

        public FootViewHolder(View itemView) {
            super(itemView);
            mfoothint = (TextView) itemView.findViewById(R.id.foot_hint);

        }
    }

  /*  public  void Sory(final int SORY_CODE){
        switch (SORY_CODE){
            case I.SORT_BY_PRICE_ASC:
                Collections.sort(mList, new Comparator<NewGoodBean>() {
                    @Override
                    public int compare(NewGoodBean left, NewGoodBean right) {
                        Log.e("main", "调用了按价格的升序排序方法"+left.getCurrencyPrice());
                        notifyDataSetChanged();
                        return (int) (getMoneyInt(left.getCurrencyPrice())-getMoneyInt(right.getCurrencyPrice()));
                    }
                });
                break;
            case I.SORT_BY_PRICE_DESC:
                Collections.sort(mList, new Comparator<NewGoodBean>() {
                    @Override
                    public int compare(NewGoodBean left, NewGoodBean right) {
                        Log.e("main", "调用了按价格的降序排序方法"+left.getCurrencyPrice());
                        notifyDataSetChanged();
                        return (int) (getMoneyInt(right.getCurrencyPrice())-getMoneyInt(left.getCurrencyPrice()));
                    }
                });
                break;
            case I.SORT_BY_ADDTIME_DESC:
                Collections.sort(mList, new Comparator<NewGoodBean>() {
                    @Override
                    public int compare(NewGoodBean left, NewGoodBean right) {
                        notifyDataSetChanged();
                        return (int) (left.getAddTime()-right.getAddTime());
                    }
                });
                break;
            case I.SORT_BY_ADDTIME_ASC:
                Collections.sort(mList, new Comparator<NewGoodBean>() {
                    @Override
                    public int compare(NewGoodBean left, NewGoodBean right) {
                        notifyDataSetChanged();
                        return (int) (right.getAddTime()-left.getAddTime());
                    }
                });
                break;
        }

    }*/

   


}
