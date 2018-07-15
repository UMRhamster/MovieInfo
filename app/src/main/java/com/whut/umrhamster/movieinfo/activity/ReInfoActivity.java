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
import com.whut.umrhamster.movieinfo.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_info);
        ButterKnife.bind(this);

        initData();
        initEvent();
    }
    private void initData(){
        editTextNiCheng.setText("小兔叽呀");  //测试用  预计从shareprefenence中获取
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
                }else {
                    Toast.makeText(ReInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    //进行密码修改
                    //修改数据库
                    //修改sharepreference
                }
            }
        });
    }
}
