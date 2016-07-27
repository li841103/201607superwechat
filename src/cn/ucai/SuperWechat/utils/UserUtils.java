package cn.ucai.SuperWechat.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.SuperWechat.SuperWeChatApplication;
import cn.ucai.SuperWechat.applib.controller.HXSDKHelper;
import cn.ucai.SuperWechat.DemoHXSDKHelper;
import cn.ucai.SuperWechat.R;
import cn.ucai.SuperWechat.bean.MemberUserAvatar;
import cn.ucai.SuperWechat.bean.UserAvatar;
import cn.ucai.SuperWechat.domain.User;
import cn.ucai.SuperWechat.widget.I;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }


	/**
	 * 根据username获取相应UserAvatar
	 * @param username
	 * @return
	 */
	public static UserAvatar getAppUserInfo(String username){
		UserAvatar user = SuperWeChatApplication.getInstance().getStringUserAvatarMap().get(username);
		if(user == null){
			user = new UserAvatar(username);
		}
		return user;
	}

	public static MemberUserAvatar getAppMemberInfo(String hxid,String username){
		MemberUserAvatar member=null;
		HashMap<String, MemberUserAvatar> members = SuperWeChatApplication.getInstance().getMemberMap().get(hxid);
		if(members==null || members.size()<0){
			return null;
		}else{
			member = members.get(username);
		}
		return member;
	}
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }

	/**
	 * 设置用户头像
	 * @param username
	 */
	public static void setAppUserAvatar(Context context, String username, ImageView imageView){
		String user=username;
		String path =getUserAvatarPath(user);
		if(user != null&&path!=null){
			path = UserUtils.getUserAvatarPath(user);
			Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(imageView);
		}else{
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}

	public static void setAppGroupAvatar(Context context, String hxid, ImageView imageView){
		String user=hxid;
		String path =getUserAvatarPath(user);
		if(user != null&&path!=null){
			path = UserUtils.getGroupAvatarPath(user);
			Picasso.with(context).load(path).placeholder(R.drawable.group_icon).into(imageView);
		}else{
			Picasso.with(context).load(R.drawable.group_icon).into(imageView);
		}
	}

	public static String getGroupAvatarPath(String hxid){
		StringBuilder sb = new StringBuilder();
		sb.append(I.SERVER_ROOT).append(I.QUESTION).append(I.KEY_REQUEST).append(I.EQUAL)
				.append(I.REQUEST_DOWNLOAD_AVATAR)
				.append(I.ALT).append(I.NAME_OR_HXID)
				.append(I.EQUAL).append(hxid)
				.append(I.ALT).append(I.AVATAR_TYPE)
				.append(I.EQUAL)
				.append(I.AVATAR_TYPE_GROUP_PATH);

		Log.i("main", sb.toString());
		return sb.toString();
	}


	public static String getUserAvatarPath(String username){
		StringBuilder sb = new StringBuilder();
		sb.append(I.SERVER_ROOT).append(I.QUESTION).append(I.KEY_REQUEST).append(I.EQUAL)
				.append(I.REQUEST_DOWNLOAD_AVATAR)
				.append(I.ALT).append(I.NAME_OR_HXID)
				.append(I.EQUAL).append(username)
				.append(I.ALT).append(I.AVATAR_TYPE)
				.append(I.EQUAL)
				.append(I.AVATAR_TYPE_USER_PATH);

		Log.i("main", sb.toString());
		return sb.toString();
	}
    
    /**
	 * 设置当前用户头像
	 */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }

	/**
	 * 设置用户好友的昵称
	 */
	public static void setAppUserNick(String username,TextView textView){
		UserAvatar user = getAppUserInfo(username);
		if(user != null){
			if(user.getMUserNick()!=null){
				textView.setText(user.getMUserNick());
			}else{
				textView.setText(username);
			}
		}else{
			textView.setText(username);
		}
	}

	public static void setAppUserNick(UserAvatar user,TextView textView){
		if(user != null){
			if(user.getMUserNick()!=null){
				textView.setText(user.getMUserNick());
			}else{
				textView.setText(user.getMUserName());
			}
		}else{
			textView.setText(user.getMUserName());
		}
	}



	/**
	 * 设置当前用户头像
	 */
	public static void setAppCurrentUserAvatar(Context context, ImageView imageView) {
		String userName = SuperWeChatApplication.getInstance().getUserName();
		setAppUserAvatar(context, userName, imageView);
	}
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }


	public static void setAppCurrentUserNick(TextView textView){
		String userName = SuperWeChatApplication.getInstance().getUserName();
		User user = UserUtils.getUserInfo(userName);
		if(user != null){
			if(user.getNick()!=null){
				textView.setText(user.getNick());
			}else{
				textView.setText(user.getUsername());
			}
		}else{
			textView.setText(user.getUsername());

		}
	}
    
    /**
     * 保存或更新某个用户
     * @param
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}

	public static void setAppMemberNick(String hxid, String username, TextView tv_usernick) {
		MemberUserAvatar member = UserUtils.getAppMemberInfo(hxid, username);
		if(member==null){
			Log.i("main,", "member==null");
		}

		if(member != null){
			if(member.getMUserNick()!=null){
				tv_usernick.setText(member.getMUserNick());
			}else{
				tv_usernick.setText(member.getMUserName());
			}
		}else{
			tv_usernick.setText(member.getMUserName());
		}
	}
}
