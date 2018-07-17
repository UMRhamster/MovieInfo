package com.whut.umrhamster.movieinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whut.umrhamster.movieinfo.R;

import java.util.List;

/**
 * Created by 12421 on 2018/7/16.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> historyList;  //搜索历史记录
    private Context context;

    private OnItemClickListener onItemClickListener;

    public SearchHistoryAdapter(Context context, List<String> historyList){
        this.context = context;
        this.historyList = historyList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_rv_item_search_history,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick((int)view.getTag());
//                Log.d("SearchAdapter","条目");
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == historyList.size()){
            ((ViewHolder)holder).textViewContent.setText("清除搜索记录");
            ((ViewHolder)holder).imageViewIcon.setImageResource(R.drawable.search_history_delete_all);
            ((ViewHolder)holder).imageViewDelete.setVisibility(View.GONE);
        }else {
            ((ViewHolder)holder).textViewContent.setText(historyList.get(position));
            ((ViewHolder)holder).imageViewDelete.setOnClickListener(new View.OnClickListener() { // x 按钮点击事件  删除单条历史记录
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemDelete(position);
                }
            });
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return historyList == null || historyList.isEmpty()?0:historyList.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewIcon;  //前面图标
        ImageView imageViewDelete;  //删除按钮
        TextView textViewContent;   //历史记录内容
        View view; //下划线
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.custom_rv_item_search_iv);
            imageViewDelete = itemView.findViewById(R.id.custom_rv_item_search_delete);
            textViewContent = itemView.findViewById(R.id.custom_rv_item_search_tv);
            view = itemView.findViewById(R.id.custom_rv_item_search_v);
        }
    }

    //
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
