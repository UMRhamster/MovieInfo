package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.movieinfo.R;
import com.whut.umrhamster.movieinfo.model.MovieSimple;
import com.whut.umrhamster.movieinfo.util.CelebrityUtil;
import com.whut.umrhamster.movieinfo.util.MovieUtil;

import java.util.List;

/**
 * Created by 12421 on 2018/7/16.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MovieSimple> simpleList;

    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(Context context, List<MovieSimple> simpleList){
        this.context = context;
        this.simpleList = simpleList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_search_search,parent,false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).textViewTitle.setText(simpleList.get(position).getTitle());
        ((ViewHolder)holder).textViewYear.setText(simpleList.get(position).getYear());
        ((ViewHolder)holder).textViewGenres.setText(simpleList.get(position).getGenres());
        ((ViewHolder)holder).textViewDirectors.setText(CelebrityUtil.List2String(simpleList.get(position).getDirectors()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return simpleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;      //电影名
        TextView textViewGenres;     //电影类型
        TextView textViewYear;       //电影年代
        TextView textViewDirectors;  //导演
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.custom_rv_item_search_search_title);
            textViewGenres = itemView.findViewById(R.id.custom_rv_item_search_search_genres);
            textViewYear = itemView.findViewById(R.id.custom_rv_item_search_search_year);
            textViewDirectors = itemView.findViewById(R.id.custom_rv_item_search_search_directors);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
