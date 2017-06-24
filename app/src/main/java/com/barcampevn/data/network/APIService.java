package com.barcampevn.data.network;

import com.barcampevn.data.models.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andranikas on 5/17/2017.
 */

public interface APIService {

    @GET("schedule")
    Call<List<Schedule>> getSchedules();
}
