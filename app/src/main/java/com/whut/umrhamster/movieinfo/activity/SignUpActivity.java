package com.whut.umrhamster.movieinfo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.User;
import com.whut.umrhamster.movieinfo.util.NetUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.ac_signup_rl_name_et)
    EditText editTextName;
    @BindView(R.id.ac_signup_rl_password_et)
    EditText editTextPassword;
    @BindView(R.id.ac_signup_rl_password_queren_et)
    EditText editTextPasswordQueren;
    @BindView(R.id.ac_signup_rl_nickname_et)
    EditText editTextNickName;
    @BindView(R.id.ac_signup_btn_zhuce)
    TextView textViewZhuce;
    @BindView(R.id.ac_signup_btn_denglu)
    TextView textViewDenglu;

    private boolean isSignUp = false;  //处理点击按钮网络延迟问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //取消半透明
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initEvent();
    }

    private void initEvent(){
        //点击注册按钮
        textViewZhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpCheck(editTextName.getText().toString(),editTextPassword.getText().toString()
                        ,editTextPasswordQueren.getText().toString(),editTextNickName.getText().toString());
            }
        });
        //点击登录按钮
        textViewDenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void signUpCheck(final String name, final String password, String passwordAgain, String nickname){
        if (!isSignUp){
            isSignUp = true;
            if (!localCheck(name,password,passwordAgain,nickname)){
                isSignUp = false;
                return;
            }
            if (!NetUtil.checkNetState(SignUpActivity.this)){
                Toast.makeText(SignUpActivity.this,"网络连接不可用，请检查网络设置",Toast.LENGTH_SHORT).show();
                isSignUp = false;
                return;
            }
            //云数据库上的验证
            BmobQuery<User> queryName = new BmobQuery<>();
            queryName.addWhereEqualTo("name",name);

            queryName.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null){
                        if (list.size() > 0){
                            isSignUp = false;
                            Toast.makeText(SignUpActivity.this,"该用户名已存在",Toast.LENGTH_SHORT).show();
                        }else {
                            User user = new User();
                            user.setName(name);
                            user.setPassword(password);
                            user.setNickname(editTextNickName.getText().toString());
                            user.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null){
                                        isSignUp = false;
                                        Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SignUpActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }
    private boolean localCheck(String name, String password, String passwordAgain, String nickName){
        String pattern = "[A-Za-z0-9]+";
        if (name.equals("") || password.equals("") || passwordAgain.equals("")){
            Toast.makeText(SignUpActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (!password.equals(passwordAgain)){
            Toast.makeText(SignUpActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(name.matches(pattern) && password.matches(pattern) && passwordAgain.matches(pattern))){
            Toast.makeText(SignUpActivity.this,"用户名或密码不合法",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nickName.equals("")){
            Toast.makeText(SignUpActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else if (!nickName.matches("[a-zA-Z\u4e00-\u9fa5]+")){
            Toast.makeText(SignUpActivity.this,"昵称不合法",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_do_nothing,R.anim.anim_fg_back_out);
    }
}
