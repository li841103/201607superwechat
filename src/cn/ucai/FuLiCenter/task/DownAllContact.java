package cn.ucai.FuLiCenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.bean.Result;
import cn.ucai.FuLiCenter.bean.UserAvatar;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/7/20.
 */
public class DownAllContact {
    public DownAllContact() {

    }

    Context context;

    public DownAllContact(Context context) {
        this.context = context;
    }

    public void exec(String username){
        Log.i("main", "用户名：" + username);
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = Utils.getListResultFromJson(s, UserAvatar.class);
                        List<UserAvatar> list = (List<UserAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            FuLiCenterApplication.getInstance().setUserAvatars(list);
                            Map<String, UserAvatar> map = FuLiCenterApplication.getInstance().getStringUserAvatarMap();
                            for(UserAvatar u:list){
                                map.put(u.getMUserName(), u);
                            }
                            context.sendStickyBroadcast(new Intent("update_contact_list"));                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
         }

}
