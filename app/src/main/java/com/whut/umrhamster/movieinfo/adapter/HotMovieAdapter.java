package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.Movie;
import com.whut.umrhamster.movieinfo.view.PicassoImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/7/11.
 */

public class HotMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BANNER = 2;
    private int showStatus =0;   //上拉加载条目 状态

    private int type;
    private Context context;
    private List<Movie> movieList;

    private OnItemClickListener onItemClickListener;
    public HotMovieAdapter(Context context, List<Movie> movieList, int type){
        this.context = context;
        this.movieList = movieList;
        this.type = type;
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
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }else if (viewType == TYPE_FOOTER){   //上拉加载条目
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_footer,parent,false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(view);
            return footerViewHolder;
        }else if (viewType == TYPE_BANNER){
            View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_banner,parent,false);
            BannerViewHolder bannerViewHolder = new BannerViewHolder(view);
            return bannerViewHolder;
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            Movie movieTemp = movieList.get(position-1);
            Picasso.get().load(movieTemp.getImages()).into(((ViewHolder) holder).imageViewPost);
            ((ViewHolder) holder).textViewTitle.setText(movieTemp.getTitle());
            ((ViewHolder) holder).textViewGenres.setText(movieTemp.getGenres()[0]);
            ((ViewHolder) holder).textViewSummary.setText(movieTemp.getSummary());
            ((ViewHolder) holder).textViewPinglun.setText(String.format(context.getResources().getString(R.string.rating_count),movieTemp.getRating_count()));
            ((ViewHolder) holder).textViewXiangkan.setText(String.format(context.getResources().getString(R.string.wish_count),movieTemp.getWish_count()));
            ((ViewHolder) holder).textViewKanguo.setText(String.format(context.getResources().getString(R.string.collect_count),movieTemp.getCollect_count()));
            ((ViewHolder) holder).textViewYear.setText(String.format(context.getResources().getString(R.string.year),movieTemp.getYear()));
            if (type == 1){
                ((ViewHolder) holder).textViewRank.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).imageViewRank.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).textViewRank.setText(String.valueOf(position));
                switch (position){
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
            }else {
                ((ViewHolder) holder).textViewRank.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).imageViewRank.setVisibility(View.INVISIBLE);
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
        }else if (holder instanceof BannerViewHolder){  //轮播图处理
            List<String> urlList = new ArrayList<>(3);
            List<String> nameList = new ArrayList<>(3);
            for (int i=0;i<movieList.size();i++){
                if (i == 3){
                    break;
                }
                nameList.add(movieList.get(i).getTitle());
                urlList.add(movieList.get(i).getImages());
            }
            ((BannerViewHolder)holder).banner.setImageLoader(new PicassoImageLoader());
            ((BannerViewHolder)holder).banner.setImages(urlList);  //设置轮播图片
            ((BannerViewHolder)holder).banner.setBannerTitles(nameList);  //设置标题
            ((BannerViewHolder)holder).banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE); //设置指示器位置
            ((BannerViewHolder)holder).banner.setBannerAnimation(Transformer.Default);
            ((BannerViewHolder)holder).banner.setDelayTime(2500);//轮播时间
            ((BannerViewHolder)holder).banner.setIndicatorGravity(BannerConfig.RIGHT);  //设置指示器位置

            ((BannerViewHolder)holder).banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    onItemClickListener.onItemClick(position);
                }
            });

            ((BannerViewHolder)holder).banner.start();
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
        }else if (position == 0){
            return TYPE_BANNER;
        }else {
            return TYPE_ITEM;
        }
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
    static class BannerViewHolder extends RecyclerView.ViewHolder{
        Banner banner;
        public BannerViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.custom_rv_banner_banner);
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
