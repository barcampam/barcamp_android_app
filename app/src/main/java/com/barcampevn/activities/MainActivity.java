package com.barcampevn.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.barcampevn.R;
import com.barcampevn.adapters.SchedulesAdapter;
import com.barcampevn.data.models.Schedule;
import com.barcampevn.data.network.APIHelper;
import com.barcampevn.helpers.SpacesItemDecoration;
import com.barcampevn.helpers.TimeHelper;
import com.barcampevn.helpers.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_VIEW;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by andranikas on 5/17/2017.
 */

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int FIRST_DAY = 27;
    private static final int SECOND_DAY = 28;
    private static final String[] ROOMS = {"Big Hall", "213W", "214W", "208E", "308E"};
    private static final String[] TIMES = {"10:00", "10:30", "11:00", "11:30", "12:00",  "12:30", "13:00",  "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};

    private SparseArray<List<Schedule>> mArray;
    private SchedulesAdapter mAdapter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.first_day) ImageView mFirstDay;
    @BindView(R.id.second_day) ImageView mSecondDay;
    @BindView(R.id.days_cover) ImageView mDays;
    @BindView(R.id.days_container) FrameLayout mDaysContainer;
    @BindView(R.id.progress) ProgressBar mProgress;

    private View.OnClickListener mOnClickListener = view -> {
        Schedule schedule = mAdapter.getItem((int) view.getTag());
        long delay = TimeHelper.delay(schedule.getTimeFrom().getDate());
        if (!schedule.isDefault() && delay > 0)
            UIHelper.showSnackBar(mDaysContainer, R.string.attend_message, R.string.attend, view1 -> UIHelper.scheduleNotification(getBaseContext(), schedule, TimeHelper.triggerAtMillis(delay)));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_stream) {
            Intent browserIntent = new Intent(ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCz4Gaw-vexemY-WtB2NJu6w"));
            startActivity(browserIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.days_cover)
    public void daysClicked(ImageView img) {
        int day;
        if (img.getTag() == null || (int)img.getTag() == FIRST_DAY) {
            mFirstDay.setImageResource(android.R.color.transparent);
            mSecondDay.setImageResource(R.drawable.ic_magenda_28);

            day = SECOND_DAY;
        } else {
            mFirstDay.setImageResource(R.drawable.ic_magenda_27);
            mSecondDay.setImageResource(android.R.color.transparent);

            day = FIRST_DAY;
        }
        img.setTag(day);
        updateView(mArray.get(day));
    }

    private void init() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mArray = new SparseArray<>();
        mAdapter = new SchedulesAdapter(mOnClickListener);

        mSecondDay.setImageResource(android.R.color.transparent);

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, TIMES.length * (ROOMS.length + 1), GridLayoutManager.HORIZONTAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return TIMES.length;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration());
        mRecyclerView.setAdapter(mAdapter);

        for (int x = 0; x < ROOMS.length; x++) {
            String room = ROOMS[x];
            int resID = getResources().getIdentifier("room" + (x + 1), "id", getPackageName());
            TextView roomTextView = (TextView) findViewById(resID);
            roomTextView.setText(room);
        }

        getSchedules();
    }

    private void getSchedules() {
        mProgress.setVisibility(VISIBLE);
        APIHelper.getService().getSchedules().enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull Response<List<Schedule>> response) {
                parseData(response.body());
                mProgress.setVisibility(GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<Schedule>> call, Throwable t) {
                mProgress.setVisibility(GONE);
                UIHelper.showSnackBar(mRecyclerView, t.getMessage());
            }
        });
    }

    private void parseData(List<Schedule> schedules) {
        List<Schedule> firstDaySchedules = Flowable.fromIterable(schedules).filter(schedule -> TimeHelper.day(schedule.getTimeFrom().getDate()) == FIRST_DAY).toList().blockingGet();
        List<Schedule> secondDaySchedules = Flowable.fromIterable(schedules).filter(schedule -> TimeHelper.day(schedule.getTimeFrom().getDate()) == SECOND_DAY).toList().blockingGet();

        firstDaySchedules = sortSchedules(firstDaySchedules);
        secondDaySchedules = sortSchedules(secondDaySchedules);

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day <= FIRST_DAY) {
            updateView(firstDaySchedules);
        } else {
            mFirstDay.setImageResource(android.R.color.transparent);
            mSecondDay.setImageResource(R.drawable.ic_magenda_28);
            mDays.setTag(SECOND_DAY);

            updateView(secondDaySchedules);
        }

        if (day == FIRST_DAY || day == SECOND_DAY) {
            mRecyclerView.scrollToPosition(scrollingPosition());
        }

        mArray.put(FIRST_DAY, firstDaySchedules);
        mArray.put(SECOND_DAY, secondDaySchedules);
    }

    private List<Schedule> sortSchedules(List<Schedule> schedules) {
        List<Schedule> sortedSchedules = new ArrayList<>();
        for (String time : TIMES) {
            Schedule schedule = new Schedule();
            schedule.setRoom(time);
            schedule.setTime(true);
            sortedSchedules.add(schedule);

            for (String room : ROOMS) {
                Schedule defaultSchedule = new Schedule();
                defaultSchedule.setDefault(true);
                for (Schedule schedule1 : schedules) {
                    if (schedule1.getRoom().equalsIgnoreCase(room)) {
                        Date header = TimeHelper.dateFromHour(time);
                        Date start = TimeHelper.dateFromHour(TimeHelper.hourDate(schedule1.getTimeFrom().getDate()));
                        Date end = TimeHelper.dateFromHour(TimeHelper.hourDate(schedule1.getTimeTo().getDate()));

                        if (TimeHelper.isEqual(start, header)) {
                            defaultSchedule = schedule1;
                        } else if (TimeHelper.isAfter(header, start) && TimeHelper.isBefore(header, end)){
                            defaultSchedule = schedule1;
                        }
                    }
                }
                sortedSchedules.add(defaultSchedule);
            }
        }

        return sortedSchedules;
    }

    private void updateView(List<Schedule> schedules) {
        if (schedules != null) {
            mAdapter.addDataSource(schedules);
            mAdapter.notifyDataSetChanged();
        }
    }

    private int scrollingPosition() {
        int position = 0;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        if (hour >= 10 && hour < 19) {
            String time = "" + hour + ":";
            if (minutes >= 30) {
                time += "30";
            } else {
                time += "00";
            }
            position = Arrays.asList(TIMES).indexOf(time);
            position = position > -1 ? position : 0;
        }

        return position * 6;
    }
}
