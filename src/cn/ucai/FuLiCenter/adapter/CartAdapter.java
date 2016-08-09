package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.activity.Boutique_DetailsActivity;
import cn.ucai.FuLiCenter.bean.BoutiqueBean;
import cn.ucai.FuLiCenter.bean.CartBean;
import cn.ucai.FuLiCenter.bean.GoodDetailsBean;
import cn.ucai.FuLiCenter.utils.ImageUtils;
import cn.ucai.FuLiCenter.widget.I;


/**
 * Created by Zhou on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<CartBean> mList;
    CartViewHolder mCartViewHolder;
    FootViewHolder mFootViewHolder;
    String footer;


    public void setFooter(String footer) {
        this.footer = footer;
    }

    public CartAdapter(Context mcontext, List<CartBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<CartBean>();
        this.mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
    /*    if(viewType==I.TYPE_FOOTER){
            Log.i("main", "TYPE_FOOTER！！！！！！");
            View view = LayoutInflater.from(mcontext).inflate(R.layout.foot_item, null, false);
            holder = new FootViewHolder(view);
        }
        if(viewType==I.TYPE_ITEM){
            Log.i("main", "进入了TYPE_ITEM！");*/
            View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_item, null, false);
            holder = new CartViewHolder(view);
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
        if(holder instanceof CartViewHolder){
            Log.i("main", "RecyclerView.ViewHolder holder");
            final CartBean cartBean = mList.get(position);
            GoodDetailsBean goods = cartBean.getGoods();
            mCartViewHolder = (CartViewHolder) holder;
            mCartViewHolder.isCheckBox.setChecked(true);
            ImageUtils.setBoutiqueImage(mcontext,goods.getGoodsThumb(),mCartViewHolder.mivImage);
            mCartViewHolder.mtv_name.setText(goods.getGoodsName());
            mCartViewHolder.mtv_num.setText("(1)");
            mCartViewHolder.mtv_money.setText(goods.getCurrencyPrice());
        }
        if(holder instanceof FootViewHolder){
            mFootViewHolder = (FootViewHolder) holder;
            mFootViewHolder.mfoothint.setText(footer);
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?1:mList.size()+1;
    }

    public void initData(ArrayList<CartBean> arr,int DOWN_CODE) {
        if(arr!=null&&DOWN_CODE==0){
            Log.i("main", "mList!=null");
            mList.clear();
            mList.addAll(arr);
            notifyDataSetChanged();
        }
        if(arr!=null&&DOWN_CODE==1){
            mList.addAll(arr);
            notifyDataSetChanged();
        }
    }


    class CartViewHolder extends RecyclerView.ViewHolder{
        CheckBox isCheckBox;
        ImageView mivImage;
        TextView mtv_name,mtv_num,mtv_money;
        Button mtn_reduce,mtn_add;
        public CartViewHolder(View itemView) {
            super(itemView);
            isCheckBox = (CheckBox) itemView.findViewById(R.id.cb_checkbox);
            mivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            mtv_name = (TextView) itemView.findViewById(R.id.tv_name);
            mtv_num = (TextView) itemView.findViewById(R.id.tv_num);
            mtn_reduce = (Button) itemView.findViewById(R.id.tv_reduce);
            mtn_add = (Button) itemView.findViewById(R.id.tv_add);
            mtv_money = (TextView) itemView.findViewById(R.id.tv_money);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder{
        TextView mfoothint;

        public FootViewHolder(View itemView) {
            super(itemView);
            mfoothint = (TextView) itemView.findViewById(R.id.foot_hint);
        }
    }



}
