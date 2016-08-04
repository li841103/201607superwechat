package cn.ucai.FuLiCenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.activity.Boutique_DetailsActivity;
import cn.ucai.FuLiCenter.bean.BoutiqueBean;
import cn.ucai.FuLiCenter.bean.NewGoodBean;
import cn.ucai.FuLiCenter.utils.ImageUtils;
import cn.ucai.FuLiCenter.widget.I;


/**
 * Created by Zhou on 2016/8/1.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mcontext;
    List<BoutiqueBean> mList;
    BoutiqueViewHolder mBoutiqueViewHolder;
    FootViewHolder mFootViewHolder;
    String footer;


    public void setFooter(String footer) {
        this.footer = footer;
    }

    public BoutiqueAdapter(Context mcontext, List<BoutiqueBean> mList) {
        this.mcontext = mcontext;
        this.mList = new ArrayList<BoutiqueBean>();
        this.mList.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(viewType==I.TYPE_FOOTER){
            Log.i("main", "TYPE_FOOTER！！！！！！");
            View view = LayoutInflater.from(mcontext).inflate(R.layout.foot_item, null, false);
            holder = new FootViewHolder(view);
        }
        if(viewType==I.TYPE_ITEM){
            Log.i("main", "进入了TYPE_ITEM！");
            View view = LayoutInflater.from(mcontext).inflate(R.layout.activity_jingxuan, null, false);
            holder = new BoutiqueViewHolder(view);
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
        if(holder instanceof BoutiqueViewHolder){
            Log.i("main", "RecyclerView.ViewHolder holder");
            final BoutiqueBean boutiqueBean = mList.get(position);
            mBoutiqueViewHolder = (BoutiqueViewHolder) holder;
            mBoutiqueViewHolder.mtvName.setText(boutiqueBean.getName());
            mBoutiqueViewHolder.mtvDesc.setText(boutiqueBean.getDescription());
            mBoutiqueViewHolder.mtvTitle.setText(boutiqueBean.getTitle());

            ImageUtils.setBoutiqueImage(mcontext,boutiqueBean.getImageurl(),mBoutiqueViewHolder.mivImage);
            mBoutiqueViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcontext.startActivity(new Intent(mcontext,
                            Boutique_DetailsActivity.class).putExtra(D.Boutique.KEY_ID,
                            boutiqueBean.getId()).putExtra(D.Boutique.KEY_NAME,boutiqueBean.getName()));
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
        return mList==null?1:mList.size()+1;
    }

    public void initData(ArrayList<BoutiqueBean> arr,int DOWN_CODE) {
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


    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        ImageView mivImage;
        TextView mtvDesc,mtvTitle,mtvName;
        RelativeLayout layout;
        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            mtvDesc= (TextView) itemView.findViewById(R.id.tv_desc);
            mivImage = (ImageView) itemView.findViewById(R.id.boutique_image);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mtvName = (TextView) itemView.findViewById(R.id.tv_name);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_boutique);
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
