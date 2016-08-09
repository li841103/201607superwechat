package cn.ucai.FuLiCenter.task;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.FuLiCenter.D;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.bean.CartBean;
import cn.ucai.FuLiCenter.bean.GoodDetailsBean;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/7/20.
 */
public class DownAllCartTask {
    public DownAllCartTask() {

    }

    Context context;

    public DownAllCartTask(Context context) {
        this.context = context;
    }

    public void exec(String username,final int GoodsId){
        Log.i("main", "用户名：" + username);
        final OkHttpUtils2<CartBean[]> utils = new OkHttpUtils2<CartBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,username)
                .addParam(I.PAGE_ID,String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] CartBeanArr) {
                        ArrayList<CartBean> cartBeenList = Utils.array2List(CartBeanArr);
                        List<CartBean> FuLiCenterCartBeanList = FuLiCenterApplication.getInstance().getCartBeanList();
                        for(final CartBean cart:cartBeenList){
                            if(!FuLiCenterCartBeanList.contains(cart)){
                                OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
                                utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                                        .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(GoodsId))
                                        .targetClass(GoodDetailsBean.class)
                                        .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                                            @Override
                                            public void onSuccess(GoodDetailsBean result) {
                                                if(result!=null){
                                                    cart.setGoods(result);
                                                }
                                            }

                                            @Override
                                            public void onError(String error) {

                                            }
                                        });
                                FuLiCenterCartBeanList.add(cart);
                            }else{
                                FuLiCenterCartBeanList.get(FuLiCenterCartBeanList.indexOf(cart)).setChecked(cart.isChecked());
                                FuLiCenterCartBeanList.get(FuLiCenterCartBeanList.indexOf(cart)).setCount(cart.getCount());
                            }
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
         }

}
