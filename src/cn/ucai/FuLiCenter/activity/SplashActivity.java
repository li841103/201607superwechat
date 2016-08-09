package cn.ucai.FuLiCenter.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import cn.ucai.FuLiCenter.DemoHXSDKHelper;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.FuLiCenterApplication;
import cn.ucai.FuLiCenter.bean.Result;
import cn.ucai.FuLiCenter.bean.UserAvatar;
import cn.ucai.FuLiCenter.db.UserDao;

import cn.ucai.FuLiCenter.task.DownAllContact;
import cn.ucai.FuLiCenter.task.DownCollectCountTask;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private RelativeLayout rootLayout;
	private TextView versionText;

	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);

		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		versionText = (TextView) findViewById(R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();

					String userName = FuLiCenterApplication.getInstance().getUserName();
					UserDao dao = new UserDao(SplashActivity.this);
					UserAvatar userAvatar = dao.getUserAvatar(userName);
					Log.i("main", "这是闪屏界面输出的信息，userAvatar的信息如下：" + userAvatar);
					if(userAvatar==null){
						OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
						utils.setRequestUrl(I.REQUEST_FIND_USER)
								.addParam(I.User.USER_NAME,userName)
								.targetClass(String.class)
								.execute(new OkHttpUtils2.OnCompleteListener<String>() {
									@Override
									public void onSuccess(String s) {
										Result result = Utils.getResultFromJson(s, UserAvatar.class);
										if(result!=null){
											UserAvatar userAvatar = (UserAvatar) result.getRetData();
											if(userAvatar!=null){
												FuLiCenterApplication.getInstance().setUserAvatar(userAvatar);
												FuLiCenterApplication.currentUserNick = userAvatar.getMUserNick();
											}else{

											}
										}


									}

									@Override
									public void onError(String error) {
										Log.i("main", "error:" + error);
									}
								});
					}else{
						FuLiCenterApplication.getInstance().setUserAvatar(userAvatar);
						FuLiCenterApplication.currentUserNick = userAvatar.getMUserNick();
					}
					new DownAllContact(SplashActivity.this).exec(userName);
					new DownCollectCountTask(SplashActivity.this).exec(userName);
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, FuLiCenterActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, FuLiCenterActivity.class));
					finish();
				}
			}
		}).start();

	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}
}
