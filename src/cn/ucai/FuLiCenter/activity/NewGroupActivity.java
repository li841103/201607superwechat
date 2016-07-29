/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.FuLiCenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import cn.ucai.FuLiCenter.R;
import cn.ucai.FuLiCenter.SuperWeChatApplication;
import cn.ucai.FuLiCenter.bean.GroupAvatar;
import cn.ucai.FuLiCenter.bean.Result;
import cn.ucai.FuLiCenter.listener.OnSetAvatarListener;
import cn.ucai.FuLiCenter.utils.OkHttpUtils2;
import cn.ucai.FuLiCenter.utils.Utils;
import cn.ucai.FuLiCenter.widget.I;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

public class NewGroupActivity extends BaseActivity {
	private EditText groupNameEditText;
	private ProgressDialog progressDialog;
	private EditText introductionEditText;
	private CheckBox checkBox;
	private CheckBox memberCheckbox;
	private LinearLayout openInviteContainer;
	private OnSetAvatarListener mOnSetAvatarListener;
	private String avatarName;
	private static int GROUP_CODE=100;
	private ImageView mIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		mIcon= (ImageView) findViewById(R.id.Icon);
		groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
		checkBox = (CheckBox) findViewById(R.id.cb_public);
		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
		openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					openInviteContainer.setVisibility(View.INVISIBLE);
				}else{
					openInviteContainer.setVisibility(View.VISIBLE);
				}
			}
		});

		findViewById(R.id.layout_rl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mOnSetAvatarListener = new OnSetAvatarListener(NewGroupActivity.this, R.id.layout_ll,getAvatarName(), I.AVATAR_TYPE_GROUP_PATH);
			}
		});
	}

	/**
	 * @param v
	 */
	public void save(View v) {
		String str6 = getResources().getString(R.string.Group_name_cannot_be_empty);
		String name = groupNameEditText.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Intent intent = new Intent(this, AlertDialog.class);
			intent.putExtra("msg", str6);
			startActivity(intent);
		} else {
			// 进通讯录选人
			startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), GROUP_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
		final String st2 = getResources().getString(R.string.Failed_to_create_groups);
		if (resultCode != RESULT_OK) {
			return;
		}
//		mIcon.setImageResource(R.drawable.appitem_del_btn_normal);
		mOnSetAvatarListener.setAvatar(requestCode,data,mIcon);
		if (requestCode == GROUP_CODE) {
			CreateGroup(data, st1, st2);
		}
	}

	private void CreateGroup(final Intent data, String st1, final String st2) {
		//新建群组
		setDialog(st1);

		new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                String[] members = data.getStringArrayExtra("newmembers");
                EMGroup group;
                try {
                    if(checkBox.isChecked()){
                        //创建公开群，此种方式创建的群，可以自由加入
                        //创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
                        group= EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true,200);
                    }else{
                        //创建不公开群
                        group=EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, memberCheckbox.isChecked(),200);
                    }
                    Log.i("main", "hxid==" + group.getGroupId());

					createMyGroup(group.getGroupId(),groupName,desc,members);

                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
	}

	private void createMyGroup(String groupId, String groupName, String desc, final String[] members) {
		//http://127.0.0.1:8080/SuperWeChatServer/Server?request=
		// create_group&m_group_hxid=&m_group_name=&m_group_description=&m_group_owner=
		// &m_group_is_public=&m_group_allow_invites=
		File file = new File(OnSetAvatarListener.getAvatarPath(NewGroupActivity.this,
				I.AVATAR_TYPE_GROUP_PATH),avatarName+I.AVATAR_SUFFIX_JPG);
		boolean isPublic = checkBox.isChecked();
		boolean isInvites= !checkBox.isChecked();
		String own = SuperWeChatApplication.getInstance().getUserName();
		final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
		utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
				.addParam(I.Group.HX_ID,groupId)
				.addParam(I.Group.NAME,groupName)
				.addParam(I.Group.DESCRIPTION,desc)
				.addParam(I.Group.OWNER,own)
				.addParam(I.Group.IS_PUBLIC,String.valueOf(isPublic))
				.addParam(I.Group.ALLOW_INVITES, String.valueOf(isInvites))
				.addFile(file)
				.targetClass(String.class)
				.execute(new OkHttpUtils2.OnCompleteListener<String>() {
					@Override
					public void onSuccess(String s) {
						Result result = Utils.getResultFromJson(s, GroupAvatar.class);
						if(result!=null&&result.isRetMsg()){
							if(members!=null&&members.length>0){
								addGroupMembers(result,members);
							}

						}
					}

					@Override
					public void onError(String error) {
						progressDialog.dismiss();
						//Toast.makeText(NewGroupActivity.this, st2+error, Toast.LENGTH_LONG).show();
						Log.e("main", "error=" + error);
					}
				});
	}
//http://127.0.0.1:8080/SuperWeChatServer/Server?request=
// add_group_members&m_member_user_id=&m_member_user_name=&m_member_group_hxid=

	private void addGroupMembers(Result result,String[] members) {
		GroupAvatar groupavatar = (GroupAvatar) result.getRetData();
		StringBuilder sb = new StringBuilder();
		for(String s:members){
			sb.append(s).append(",");
		}
		sb.substring(0,sb.length()-1);
		final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
		utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
				.addParam(I.Member.USER_NAME,sb.toString())
				.addParam(I.Member.GROUP_HX_ID,groupavatar.getMGroupHxid())
				.targetClass(String.class)
				.execute(new OkHttpUtils2.OnCompleteListener<String>() {
					@Override
					public void onSuccess(String s) {
						Result result = Utils.getResultFromJson(s, GroupAvatar.class);
						if(result!=null&&result.isRetMsg()) {
							runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									setResult(RESULT_OK);
									finish();
								}
							});
						}

					}

					@Override
					public void onError(String error) {
						progressDialog.dismiss();
						//Toast.makeText(NewGroupActivity.this, st2+error, Toast.LENGTH_LONG).show();
						Log.e("main", "error=" + error);
					}
				});
	}

	private void setDialog(String st1) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(st1);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	public void back(View view) {
		finish();
	}

	public String getAvatarName() {
		avatarName = String.valueOf(System.currentTimeMillis());
		return avatarName;
	}
}
