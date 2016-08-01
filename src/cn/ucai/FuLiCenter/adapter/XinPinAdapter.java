package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.NewGoodBean;

/**
 * Created by Zhou on 2016/8/1.
 */
public class XinPinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<NewGoodBean> mList;
    XinPinViewHolder mXinPinViewHolder;

    public XinPinAdapter(Context mcontext, List<NewGoodBean> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
        this.mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = LayoutInflater.from(mcontext).inflate(R.layout.xinpin_item, null, false);
        holder = new XinPinViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof XinPinViewHolder){
            mXinPinViewHolder = (XinPinViewHolder) holder;
            NewGoodBean xin = mList.get(position);
            mXinPinViewHolder.mmtvMoney.setText(xin.getPromotePrice());
            mXinPinViewHolder.mmtvDesc.setText(xin.getGoodsBrief());
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }


    class XinPinViewHolder extends RecyclerView.ViewHolder{
        ImageView mmivImage;
        TextView mmtvDesc,mmtvMoney;
        public XinPinViewHolder(View itemView) {
            super(itemView);
            mmtvDesc= (TextView) itemView.findViewById(R.id.tvDesc);
            mmivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            mmtvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        }
    }
}
