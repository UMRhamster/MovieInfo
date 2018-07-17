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
import com.whut.umrhamster.movieinfo.model.MovieRank;

import java.util.List;

/**
 * Created by 12421 on 2018/7/17.
 */

public class BoxMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private int showStatus =0;   //上拉加载条目 状态

    private Context context;
    private List<MovieRank> movieList;

    private OnItemClickListener onItemClickListener;
    public BoxMovieAdapter(Context context, List<MovieRank> movieList){
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){   //正常条目
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_box,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick((int)view.getTag());
                }
            });
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }else if (viewType == TYPE_FOOTER){   //上拉加载条目
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_footer,parent,false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(view);
            return footerViewHolder;
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            MovieRank movieTemp = movieList.get(position);
            Picasso.get().load(movieTemp.getImages()).into(((ViewHolder) holder).imageViewPost);
            ((ViewHolder) holder).textViewTitle.setText(movieTemp.getTitle());
            ((ViewHolder) holder).textViewOriginalTitle.setText(movieTemp.getOriginalTitle());
            ((ViewHolder) holder).textViewGenres.setText(movieTemp.getGenres()[0]);
            ((ViewHolder) holder).textViewBox.setText(String.format(context.getResources().getString(R.string.box_box),movieTemp.getBox()));
            ((ViewHolder) holder).textViewYear.setText(String.format(context.getResources().getString(R.string.year),movieTemp.getYear()));
            ((ViewHolder) holder).textViewRank.setText(String.valueOf(position+1));
            if (movieTemp.isNew()){
                ((ViewHolder) holder).textViewIsNew.setVisibility(View.VISIBLE);
            }else {
                ((ViewHolder) holder).textViewIsNew.setVisibility(View.GONE);
            }
            switch (position+1){
                case 1:
                    ((ViewHolder) holder).imageViewRank.setImageResource(R.drawable.box_order_1);
                    break;
                case 2:
                    ((ViewHolder) holder).imageViewRank.setImageResource(R.drawable.box_order_2);
                    break;
                case 3:
                    ((ViewHolder) holder).imageViewRank.setImageResource(R.drawable.box_order_3);
                    break;
                default:
                    ((ViewHolder) holder).imageViewRank.setImageResource(R.drawable.box_order_default);
                    break;
            }

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
    public int getItemCount() {
        return movieList.isEmpty()?0:movieList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position  == movieList.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    //正常条目
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPost;    //海报
        TextView textViewTitle;     //电影名
        TextView textViewGenres;    //电影类型
        TextView textViewOriginalTitle;   //电影原名
        TextView textViewBox;       //票房
        TextView textViewYear;      //年代
        TextView textViewIsNew;     //是否新电影
        ImageView imageViewRank;
        TextView textViewRank;
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.rv_item_box_iv);
            textViewTitle = itemView.findViewById(R.id.rv_item_box_title);
            textViewGenres = itemView.findViewById(R.id.rv_item_box_genres);
            textViewOriginalTitle = itemView.findViewById(R.id.rv_item_box_title_original);
            textViewBox = itemView.findViewById(R.id.rv_item_box_box);
            textViewYear = itemView.findViewById(R.id.rv_item_box_year);
            imageViewRank = itemView.findViewById(R.id.rv_item_box_rank_iv);
            textViewRank = itemView.findViewById(R.id.rv_item_box_rank_tv);
            textViewIsNew = itemView.findViewById(R.id.rv_item_box_isnew);
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
