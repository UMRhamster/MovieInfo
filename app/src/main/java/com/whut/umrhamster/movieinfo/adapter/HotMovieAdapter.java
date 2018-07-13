package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.Movie;

import java.util.List;

/**
 * Created by 12421 on 2018/7/11.
 */

public class HotMovieAdapter extends RecyclerView.Adapter<HotMovieAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movieList;

    private OnItemClickListener onItemClickListener;
    public HotMovieAdapter(Context context, List<Movie> movieList){
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public HotMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_hot,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick((int)view.getTag());
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movieTemp = movieList.get(position);
        Picasso.get().load(movieTemp.getImages()).into(holder.imageViewPost);
        holder.textViewTitle.setText(movieTemp.getTitle());
        holder.textViewGenres.setText(movieTemp.getGenres()[0]);
        holder.textViewSummary.setText(movieTemp.getSummary());
        holder.textViewPinglun.setText(String.format(context.getResources().getString(R.string.rating_count),movieTemp.getRating_count()));
        holder.textViewXiangkan.setText(String.format(context.getResources().getString(R.string.wish_count),movieTemp.getWish_count()));
        holder.textViewKanguo.setText(String.format(context.getResources().getString(R.string.collect_count),movieTemp.getCollect_count()));
        holder.textViewYear.setText(String.format(context.getResources().getString(R.string.year),movieTemp.getYear()));

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPost;    //海报
        TextView textViewTitle;     //电影名
        TextView textViewGenres;    //电影类型
        TextView textViewSummary;   //简介
        TextView textViewPinglun;   //评论
        TextView textViewXiangkan;  //想看
        TextView textViewKanguo;    //看过
        TextView textViewYear;      //年代
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.rv_item_hot_iv);
            textViewTitle = itemView.findViewById(R.id.rv_item_hot_title);
            textViewGenres = itemView.findViewById(R.id.rv_item_hot_genres);
            textViewSummary = itemView.findViewById(R.id.rv_item_hot_summary);
            textViewPinglun = itemView.findViewById(R.id.rv_item_hot_pinglun_renshu);
            textViewXiangkan = itemView.findViewById(R.id.rv_item_hot_xiangkan_renshu);
            textViewKanguo = itemView.findViewById(R.id.rv_item_hot_kanguo_renshu);
            textViewYear = itemView.findViewById(R.id.rv_item_hot_year);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    //
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
