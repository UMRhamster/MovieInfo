package com.whut.umrhamster.movieinfo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.User;
import com.whut.umrhamster.movieinfo.util.SPUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReInfoActivity extends AppCompatActivity {
    @BindView(R.id.ac_reinfo_rl_nicheng_et)
    EditText editTextNiCheng;
    @BindView(R.id.ac_reinfo_tl_et_yuanmima)
    EditText editTextYuan;
    @BindView(R.id.ac_reinfo_tl_et_xinmima)
    EditText editTextXin;
    @BindView(R.id.ac_reinfo_tl_et_querenmima)
    EditText editTextQueRen;
    @BindView(R.id.ac_reinfo_btn)
    TextView textViewBtn;
    @BindView(R.id.ac_reinfo_rl_pull_iv)
    ImageView imageViewPull;
    @BindView(R.id.ac_reinfo_rl_shouqi_iv)
    ImageView imageViewShouQi;
    @BindView(R.id.ac_reinfo_tl)
    TableLayout tableLayout;
    @BindView(R.id.ac_reinfo_iv_back)
    ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_info);
        ButterKnife.bind(this);

        initData();
        initEvent();
    }
    private void initData(){
        editTextNiCheng.setText(SPUtil.loadData(ReInfoActivity.this,"user","nickname"));  //从shareprefenence中获取
    }
    private void initEvent(){
        imageViewPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.setVisibility(View.VISIBLE);
                imageViewShouQi.setVisibility(View.VISIBLE);
            }
        });
        imageViewShouQi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.setVisibility(View.GONE);
                imageViewShouQi.setVisibility(View.GONE);
            }
        });
        textViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextNiCheng.getText().toString().matches("[a-zA-Z\u4e00-\u9fa5]+")){
                    Toast.makeText(ReInfoActivity.this,"原密码不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tableLayout.getVisibility() == View.VISIBLE){
                    String yuan = editTextYuan.getText().toString();
                    String xin = editTextXin.getText().toString();
                    String queren = editTextQueRen.getText().toString();
                    if (!SPUtil.loadData(ReInfoActivity.this,"user","password").equals(yuan)){
                        Toast.makeText(ReInfoActivity.this,"原密码不正确",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (xin.isEmpty() || queren.isEmpty()){
                        Toast.makeText(ReInfoActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!xin.equals(queren)){
                        Toast.makeText(ReInfoActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                upDateInfo();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void upDateInfo(){
        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.addWhereEqualTo("name",SPUtil.loadData(ReInfoActivity.this,"user","name"));
        userQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size()>0){
                    User user = list.get(0);
                    user.setNickname(editTextNiCheng.getText().toString());
                    if (tableLayout.getVisibility() == View.VISIBLE){
                        user.setPassword(editTextQueRen.getText().toString());
                    }
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                SPUtil.saveData(ReInfoActivity.this,"user","nickname",editTextNiCheng.getText().toString());
                                SPUtil.saveData(ReInfoActivity.this,"user","password",editTextQueRen.getText().toString());
                                Toast.makeText(ReInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ReInfoActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }
}
