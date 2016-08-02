package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.ImageUtils;


/**
 * Created by Zhou on 2016/8/1.
 */
public class XinPinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<NewGoodBean> mList;
    XinPinViewHolder mXinPinViewHolder;

    public XinPinAdapter(Context mcontext, List<NewGoodBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<NewGoodBean>();
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
            NewGoodBean xin = mList.get(position);
            mXinPinViewHolder = (XinPinViewHolder) holder;
            mXinPinViewHolder.mmtvMoney.setText(xin.getCurrencyPrice());
            mXinPinViewHolder.mmtvDesc.setText(xin.getGoodsBrief());
            ImageUtils.setXinPinImage(mcontext,xin.getGoodsThumb(),mXinPinViewHolder.mmivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public void initData(ArrayList<NewGoodBean> arr) {
        if(mList!=null){
            Log.i("main", "mList!=null");
            mList.clear();
            mList.addAll(arr);
            notifyDataSetChanged();
        }
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
