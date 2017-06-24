package com.barcampevn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcampevn.R;
import com.barcampevn.data.models.Schedule;
import com.barcampevn.helpers.TimeHelper;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by andranikas on 5/17/2017.
 */

public class SchedulesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Schedule> mDataSource;
    private OnClickListener mListener;

    public SchedulesAdapter(OnClickListener listener) {
        mDataSource = Collections.emptyList();
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_header_row, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_row, parent, false);
            return new ItemViewHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Schedule schedule = getItem(position);
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.mRoom.setText(schedule.getRoom());
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            if (schedule.isDefault()) {
                itemViewHolder.mTime.setText("");
                itemViewHolder.mSpeaker.setText("");
                itemViewHolder.mTopic.setText("");
                itemViewHolder.mContainer.setBackgroundColor(itemViewHolder.itemView.getContext().getResources().getColor(android.R.color.transparent));

                itemViewHolder.itemView.setOnClickListener(null);
            } else {
                itemViewHolder.mTime.setText(TimeHelper.hourDate(schedule.getTimeFrom().getDate()) + "-" + TimeHelper.hourDate(schedule.getTimeTo().getDate()));
                itemViewHolder.mSpeaker.setText(schedule.getEnSpeaker().getSpeaker());
                itemViewHolder.mTopic.setText(schedule.getEnSpeaker().getTopic());
                itemViewHolder.mContainer.setBackground(itemViewHolder.itemView.getContext().getResources().getDrawable(R.drawable.rounded_corners_bg));

                int color;
                if (TimeHelper.isAfter(TimeHelper.currentDate(), TimeHelper.date(schedule.getTimeTo().getDate()))) {
                    color = R.color.colorGrayDark;
                } else {
                    color = R.color.colorGrayExtraDark;
                }
                itemViewHolder.mSpeaker.setTextColor(itemViewHolder.itemView.getContext().getResources().getColor(color));
                itemViewHolder.mTopic.setTextColor(itemViewHolder.itemView.getContext().getResources().getColor(color));

                itemViewHolder.itemView.setTag(position);
                itemViewHolder.itemView.setOnClickListener(mListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isTime() ? TYPE_HEADER : TYPE_ITEM;
    }

    public Schedule getItem(int position) {
        return mDataSource.get(position);
    }

    public void addDataSource(List<Schedule> dataSource) {
        mDataSource = dataSource;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView mRoom;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time) TextView mTime;
        @BindView(R.id.speaker) TextView mSpeaker;
        @BindView(R.id.topic) TextView mTopic;
        @BindView(R.id.scheduleContainer) RelativeLayout mContainer;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
