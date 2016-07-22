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
import cn.ucai.SuperWechat.bean.UserAvatar;
import cn.ucai.SuperWechat.domain.User;
import cn.ucai.SuperWechat.widget.I;

import com.squareup.picasso.Picasso;

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



	/**
	 * 设置当前用户头像
	 */
	public static void setAppCurrentUserAvatar(Context context, ImageView imageView) {
		UserAvatar avatar = UserUtils.getAppUserInfo(SuperWeChatApplication.getInstance().getUserName());
		if (avatar != null && avatar.getMAvatarId() != null) {
			Picasso.with(context).load(avatar.getMAvatarPath()).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
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
				textView.setText(user.getNick());
			}
		}else{
			textView.setText(user.getNick());

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
    
}
