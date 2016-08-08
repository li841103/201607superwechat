package cn.ucai.FuLiCenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.bean.MessageBean;
import cn.ucai.FuLiCenter.bean.Result;
import cn.ucai.FuLiCenter.bean.UserAvatar;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/7/20.
 */
public class DownCollectCountTask {
    public DownCollectCountTask() {

    }

    Context context;

    public DownCollectCountTask(Context context) {
        this.context = context;
    }

    public void exec(String username){
        Log.i("main", "用户名：" + username);
        final OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msgbean) {
                        if(msgbean!=null&&msgbean.isSuccess()){
                            FuLiCenterApplication.getInstance().setCollectcount(Integer.valueOf(msgbean.getMsg()));
                        }else{
                            FuLiCenterApplication.getInstance().setCollectcount(0);
                        }

                        context.sendStickyBroadcast(new Intent("update_contact"));

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
         }

}
