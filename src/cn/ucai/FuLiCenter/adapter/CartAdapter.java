package cn.ucai.FuLiCenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.activity.Boutique_DetailsActivity;
import cn.ucai.FuLiCenter.activity.CartActivity;
import cn.ucai.FuLiCenter.activity.CartFragment;
import cn.ucai.FuLiCenter.activity.Settlement;
import cn.ucai.FuLiCenter.bean.BoutiqueBean;
import cn.ucai.FuLiCenter.bean.CartBean;
import cn.ucai.FuLiCenter.bean.GoodDetailsBean;
import cn.ucai.FuLiCenter.bean.MessageBean;
import cn.ucai.FuLiCenter.utils.ImageUtils;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;


/**
 * Created by Zhou on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity mcontext;
    List<CartBean> mList;
    CartViewHolder mCartViewHolder;
    FootViewHolder mFootViewHolder;
    String footer;
    public static int Total,Save;





    public void setFooter(String footer) {
        this.footer = footer;
    }

    public CartAdapter(Activity mcontext, List<CartBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<CartBean>();
        this.mList.addAll(mList);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
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
            final GoodDetailsBean goods = cartBean.getGoods();
            mCartViewHolder = (CartViewHolder) holder;
            mCartViewHolder.isCheckBox.setChecked(true);
            ImageUtils.setBoutiqueImage(mcontext,goods.getGoodsThumb(),mCartViewHolder.mivImage);
            mCartViewHolder.mtv_name.setText(goods.getGoodsName());
            mCartViewHolder.mtv_num.setText("("+String.valueOf(cartBean.getCount())+")");
            mCartViewHolder.mtv_money.setText("￥"+Utils.Money(goods.getCurrencyPrice())*cartBean.getCount());
            Total += Utils.Money(goods.getCurrencyPrice()) * cartBean.getCount();
            int Currency=Utils.Money(goods.getCurrencyPrice()) * cartBean.getCount();
            int RankPrice=Utils.Money(goods.getRankPrice()) * cartBean.getCount();
            Log.i("main", "Total=" + Total + "   RankPrice=" + RankPrice);
            Save=Currency-RankPrice;
            Utils.SaveDesc(mcontext,Save);
            Utils.MoneyDesc(mcontext,Total);
            mCartViewHolder.mtn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num = mCartViewHolder.mtv_num.getText().toString();
                    int number=Integer.valueOf(num.substring(num.indexOf("(")+1,num.lastIndexOf(")")));
                    int i1=Utils.Money(goods.getCurrencyPrice())*number;
                    int y1=Utils.Money(goods.getRankPrice())*number;
                    number++;
                    int i2=Utils.Money(goods.getCurrencyPrice())*number;
                    int i3=i2-i1;
                    Total+=i3;
                    int y2=Utils.Money(goods.getRankPrice())*number;
                    int y3 = y1 - y2;
                    Log.i("main", "i2=" + i2 + " y2=" + y2);
                    Save += y3;
                    mCartViewHolder.mtv_num.setText("("+number+")");
                    mCartViewHolder.mtv_money.setText("￥"+Utils.Money(goods.getCurrencyPrice())*number);
                    Utils.MoneyDesc(mcontext,Total);
                    Utils.SaveDesc(mcontext,Save);

                }
            });
            mCartViewHolder.mtn_reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num = mCartViewHolder.mtv_num.getText().toString();
                    int number=Integer.valueOf(num.substring(num.indexOf("(")+1,num.lastIndexOf(")")));
                    int i1=Utils.Money(goods.getCurrencyPrice())*number;
                    number--;
                    if(number<=0){
                        return;
                    }
                    int i2=Utils.Money(goods.getCurrencyPrice())*number;
                    int y2=Utils.Money(goods.getRankPrice())*number;
                    int i3=i1-i2;
                    int y3 = i2 - y2;
                    Total-=i3;
                    Save -= y3;
                    mCartViewHolder.mtv_num.setText("("+number+")");
                    mCartViewHolder.mtv_money.setText("￥"+Utils.Money(goods.getCurrencyPrice())*number);
                    Utils.SaveDesc(mcontext,Save);
                    Utils.MoneyDesc(mcontext,Total);
                }
            });
            mCartViewHolder.isCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        Log.i("main", "CheckBox选中状态！");
                        Total += Utils.Money(goods.getCurrencyPrice()) * cartBean.getCount();
                        Utils.MoneyDesc(mcontext,Total);
                    }else{
                        String num = mCartViewHolder.mtv_num.getText().toString();
                        Log.i("main","在Check未选中状态，num="+num+"   单价="+goods.getCurrencyPrice()
                        +"  当前选中商品数量="+Integer.valueOf(num.substring(num.indexOf("(")+1,num.lastIndexOf(")"))));

                        Total-=Utils.Money(goods.getCurrencyPrice())*Integer.valueOf(num.substring(num.indexOf("(")+1,num.lastIndexOf(")")));
                        Utils.MoneyDesc(mcontext,Total);
                    }
                }

            } );
//

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
        Button mtn_reduce,mtn_add,mbtn_purchase;


        public CartViewHolder(View itemView) {
            super(itemView);
            mbtn_purchase = (Button) itemView.findViewById(R.id.btn_purchase);
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
