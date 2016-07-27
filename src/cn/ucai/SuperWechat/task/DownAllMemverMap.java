package cn.ucai.SuperWechat.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.SuperWechat.SuperWeChatApplication;
import cn.ucai.SuperWechat.bean.GroupAvatar;
import cn.ucai.SuperWechat.bean.MemberUserAvatar;
import cn.ucai.SuperWechat.bean.Result;
import cn.ucai.SuperWechat.utils.OkHttpUtils2;
import cn.ucai.SuperWechat.utils.Utils;
import cn.ucai.SuperWechat.widget.I;

/**
 * Created by Zhou on 2016/7/20.
 */
public class DownAllMemverMap {
    public DownAllMemverMap() {

    }

    Context context;
    String hxid;
    public DownAllMemverMap(Context context,String hxid) {
        this.context = context;
        this.hxid = hxid;
    }

    public void exec(final String hxid){
        Log.i("main", "用户名：" + hxid);
        //url=http://127.0.0.1:8080/SuperWeChatServer/Server?request=find_group_by_user_name&m_user_name=
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                   
                    // I.SERVER_URL + "?request=download_group_members_by_hxid&m_member_group_hxid=" + hxId;
                    @Override
                    public void onSuccess(String s) {
                        Log.i("main", "hxidMembers=" + s);
                        Result result = Utils.getListResultFromJson(s, MemberUserAvatar.class);
                        List<MemberUserAvatar> list = (List<MemberUserAvatar>) result.getRetData();
                        if(list==null||list.size()==0){
                            Log.i("main", "List的集合是空的");
                        }
                        if (list != null && list.size() > 0) {
                            Map<String, HashMap<String, MemberUserAvatar>> memberMap = SuperWeChatApplication.getInstance().getMemberMap();
                            if(!memberMap.containsKey(hxid)){
                                memberMap.put(hxid, new HashMap<String, MemberUserAvatar>());
                            }

                            HashMap<String, MemberUserAvatar> hxidMembers = memberMap.get(hxid);
                            for(MemberUserAvatar u:list){
                                hxidMembers.put(u.getMUserName(), u);
                            }
                            context.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
         }

}
