package com.social.trending;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class TrendsListAdapter extends RecyclerView.Adapter<TrendsListAdapter.ViewHolder> {

    private List<TrendDetail> TrendList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView count;
        public TextView name;
        public TextView url;
        public TextView promoted_content;
        public TextView query;
        public TextView tweet_volume;


        public ViewHolder(View view) {
            super(view);
            count = (TextView) view.findViewById(R.id.trend_count);
            name = (TextView) view.findViewById(R.id.trend_name);
            tweet_volume = (TextView) view.findViewById(R.id.tweet_volume);

        }
    }

    public TrendsListAdapter(List<TrendDetail> list) {
        this.TrendList = list;
    }

    @Override
    public TrendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trend_row, parent, false);

        TrendsListAdapter.ViewHolder holder = new TrendsListAdapter.ViewHolder(itemView);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TrendsListAdapter.ViewHolder holder, int position) {

        if (TrendList.size() > 1) {

            TrendDetail tD = TrendList.get(position);
            holder.count.setText(tD.count);
            holder.name.setText(tD.name);
            if (!tD.tweet_volume.equals("null")) {
                holder.tweet_volume.setText(tD.tweet_volume + " Tweets");
            }
        }

    }

    @Override
    public int getItemCount() {
        return TrendList.size();
    }

}