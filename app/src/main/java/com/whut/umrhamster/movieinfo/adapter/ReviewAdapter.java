package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.Review;
import com.whut.umrhamster.movieinfo.model.User;
import com.whut.umrhamster.movieinfo.util.SPUtil;
import com.whut.umrhamster.movieinfo.view.CircleImageView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 12421 on 2018/7/15.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Review> reviewList;
    private boolean isPersonal;

    Handler handler = new Handler();

    public ReviewAdapter(Context context, List<Review> reviewList, boolean isPersonal){
        this.context = context;
        this.reviewList = reviewList;
        this.isPersonal = isPersonal;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_review,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//        ((ViewHolder)holder).textViewName.setText();    //先从评论记录 拿到用户id,再查询到用户名
        ((ViewHolder)holder).textViewContent.setText(reviewList.get(position).getContent());
        ((ViewHolder)holder).textViewDate.setText(reviewList.get(position).getDate().getDate());
        ((ViewHolder)holder).textViewName.setText("加载中");
        if (!isPersonal){  //如果不是"我的评论",不需要显示右下角电影名
            ((ViewHolder)holder).textViewMovie.setVisibility(View.GONE);
            BmobQuery<User> userQuery = new BmobQuery<>();
            userQuery.addWhereEqualTo("name",reviewList.get(position).getUserId());
            userQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(final List<User> list, BmobException e) {
                    if (e == null && list.size()>0){
                        Picasso.with(context).load(list.get(0).getAvatars()).into(((ViewHolder)holder).circleImageView);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((ViewHolder)holder).textViewName.setText(list.get(0).getNickname());
                            }
                        });
                    }
                }
            });
        }else {
            Picasso.with(context).load(new File(SPUtil.loadData(context,"user","local_avatars"))).into(((ViewHolder)holder).circleImageView); //使用Picasso加载本地图片
            ((ViewHolder)holder).textViewMovie.setVisibility(View.VISIBLE);
            ((ViewHolder)holder).textViewName.setText(SPUtil.loadData(context,"user","nickname"));
            ((ViewHolder)holder).textViewMovie.setText(context.getResources().getString(R.string.review_movie,reviewList.get(position).getMovieName()));
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;    //圆形头像
        TextView textViewName;              //名称
        TextView textViewContent;           //内容
        TextView textViewDate;              //时间
        TextView textViewMovie;             //评论的电影
        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.custom_rv_item_review_img);
            textViewName = itemView.findViewById(R.id.custom_rv_item_review_name);
            textViewContent = itemView.findViewById(R.id.custom_rv_item_review_content);
            textViewDate = itemView.findViewById(R.id.custom_rv_item_review_date);
            textViewMovie = itemView.findViewById(R.id.custom_rv_item_review_movie);
        }
    }
}
