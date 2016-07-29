package cn.ucai.FuLiCenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.FuLiCenter.SuperWeChatApplication;
import cn.ucai.FuLiCenter.bean.GroupAvatar;
import cn.ucai.FuLiCenter.bean.Result;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * Created by Zhou on 2016/7/20.
 */
public class DownAllGroup {
    public DownAllGroup() {

    }

    Context context;

    public DownAllGroup(Context context) {
        this.context = context;
    }

    public void exec(String username){
        Log.i("main", "用户名：" + username);
        //url=http://127.0.0.1:8080/SuperWeChatServer/Server?request=find_group_by_user_name&m_user_name=
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = Utils.getListResultFromJson(s, GroupAvatar.class);
                        List<GroupAvatar> list = (List<GroupAvatar>) result.getRetData();

                        SuperWeChatApplication.getInstance().setGroupDeleteList((List<GroupAvatar>) result.getRetData());

                        Map<String, GroupAvatar> groupMap =SuperWeChatApplication.getInstance().getGroupMap();

                        if (list != null && list.size() > 0) {
                            SuperWeChatApplication.getInstance().setGroupList(list);
                        }
                        for(GroupAvatar g:list){
                            groupMap.put(g.getMAvatarUserName(), g);
                        }
                        context.sendStickyBroadcast(new Intent("update_group_list"));
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
         }

}
