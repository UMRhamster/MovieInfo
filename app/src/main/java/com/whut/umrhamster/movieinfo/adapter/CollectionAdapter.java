package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.model.Review;

import java.util.List;

/**
 * Created by 12421 on 2018/7/18.
 */

public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private int showStatus =0;   //上拉加载条目 状态

    private Context context;
    private List<Movie> movieList;

    private OnItemClickListener onItemClickListener;
    public CollectionAdapter(Context context, List<Movie> movieList){
        this.context = context;
        this.movieList = movieList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){   //正常条目
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_hot,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick((int)view.getTag());
                }
            });
            HotMovieAdapter.ViewHolder holder = new HotMovieAdapter.ViewHolder(view);
            return holder;
        }else if (viewType == TYPE_FOOTER){   //上拉加载条目
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_footer,parent,false);
            HotMovieAdapter.FooterViewHolder footerViewHolder = new HotMovieAdapter.FooterViewHolder(view);
            return footerViewHolder;
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            Movie movieTemp = movieList.get(position);
            Picasso.get().load(movieTemp.getImages()).into(((ViewHolder) holder).imageViewPost);
            ((ViewHolder) holder).textViewTitle.setText(movieTemp.getTitle());
            String[] genres = movieTemp.getGenres().split("、");
            ((ViewHolder) holder).textViewGenres.setText(genres[0]); //只取第一个标签

            ((ViewHolder) holder).textViewSummary.setText(movieTemp.getSummary());
            ((ViewHolder) holder).textViewPinglun.setText(String.format(context.getResources().getString(R.string.rating_count),movieTemp.getRating_count()));
            ((ViewHolder) holder).textViewXiangkan.setText(String.format(context.getResources().getString(R.string.wish_count),movieTemp.getWish_count()));
            ((ViewHolder) holder).textViewKanguo.setText(String.format(context.getResources().getString(R.string.collect_count),movieTemp.getCollect_count()));
            ((ViewHolder) holder).textViewYear.setText(String.format(context.getResources().getString(R.string.year),movieTemp.getYear()));

            ((ViewHolder) holder).textViewRank.setVisibility(View.GONE);
            ((ViewHolder) holder).imageViewRank.setVisibility(View.GONE);
        }else if (holder instanceof  FooterViewHolder){
            if (showStatus == 0){
                ((FooterViewHolder) holder).textView.setText("松开手指进行加载");
                ((FooterViewHolder) holder).progressBar.setVisibility(View.GONE);
            }else if (showStatus == 1){
                ((FooterViewHolder) holder).textView.setText("正在加载...");
                ((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            }else {
                ((FooterViewHolder) holder).textView.setText("已经没有更多了");
                ((FooterViewHolder) holder).progressBar.setVisibility(View.GONE);
            }
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position  == movieList.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.isEmpty()?0:movieList.size()+1;
    }
    //正常条目
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPost;    //海报
        TextView textViewTitle;     //电影名
        TextView textViewGenres;    //电影类型
        TextView textViewSummary;   //简介
        TextView textViewPinglun;   //评论
        TextView textViewXiangkan;  //想看
        TextView textViewKanguo;    //看过
        TextView textViewYear;      //年代
        ImageView imageViewRank;
        TextView textViewRank;
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
            imageViewRank = itemView.findViewById(R.id.rv_item_hot_rank_iv);
            textViewRank = itemView.findViewById(R.id.rv_item_hot_rank_tv);
        }
    }
    //上啦加载条目
    static class FooterViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView textView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.custom_rv_footer_pb);
            textView = itemView.findViewById(R.id.custom_rv_footer_tv);
        }
    }
    //改变上拉加载条目状态
    public void changeShowStatus(int value){
        showStatus = value;
        notifyItemChanged(movieList.size());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    //
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
