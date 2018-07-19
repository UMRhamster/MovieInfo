package com.whut.umrhamster.movieinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.User;
import com.whut.umrhamster.movieinfo.util.NetUtil;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.ac_login_rl_name_et)
    EditText editTextName;
    @BindView(R.id.ac_login_rl_password_et)
    EditText editTextPassword;
    @BindView(R.id.ac_login_btn)
    TextView textViewLogin;
    @BindView(R.id.ac_login_tv_new)
    TextView textViewNew;

    private User user;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //取消半透明
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initEvent();
    }

    private void initEvent(){
        //点击登陆按钮
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击进行登陆验证
                loginCheck(editTextName.getText().toString(),editTextPassword.getText().toString());
            }
        });
        //点击注册
        textViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转注册界面
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
    }

    private void loginCheck(String name, String password){
        if (!localCheck(name,password)){
            return;
        }
        if (!NetUtil.checkNetState(LoginActivity.this)){
            Toast.makeText(LoginActivity.this,"网络连接不可用，请检查网络设置",Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<User> queryName = new BmobQuery<>();
        queryName.addWhereEqualTo("name",name);
        BmobQuery<User> queryPassword = new BmobQuery<>();
        queryPassword.addWhereEqualTo("password",password);

        List<BmobQuery<User>> query = new ArrayList<>();
        query.add(queryName);
        query.add(queryPassword);

        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.and(query);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list.size() == 1){
                    user = list.get(0);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            SPUtil.saveUser(LoginActivity.this,user);
                            String[] temp = user.getAvatars().split("/");
                            BmobFile file = new BmobFile(temp[temp.length-1],"",user.getAvatars());
                            file.download(new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null){
                                        Log.d("user_avatars","用户头像下载成功:"+s);
                                        SPUtil.saveData(LoginActivity.this,"user","local_avatars",s);
                                    }
                                }
                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("user",user);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_fg_out);
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private boolean localCheck(String name, String password){
        String pattern = "[A-Za-z0-9]+";
        if (name.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (!(name.matches(pattern) && password.matches(pattern))){
            Toast.makeText(LoginActivity.this,"账号或密码不合法",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
