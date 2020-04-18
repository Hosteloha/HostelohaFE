package com.hosteloha.app.retroapi;

import com.hosteloha.app.log.HostelohaLog;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class CallbackWithRetry<T> implements Callback<T> {
    private static final int TOTAL_RETRIES = 5;
    private int retryCount = 0;

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        HostelohaLog.debugOut(" Request Failure :: " + t.getLocalizedMessage());
        if (retryCount++ < TOTAL_RETRIES) {
            HostelohaLog.debugOut("Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry(call);
        }
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }
}
