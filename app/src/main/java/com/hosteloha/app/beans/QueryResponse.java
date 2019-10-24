package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class QueryResponse {

    @SerializedName("queryStatus")
    private String mQueryStatus;

    @SerializedName("queryError")
    private String mQueryError;

    public QueryResponse(String mQueryStatus, String mQueryError) {
        this.mQueryStatus = mQueryStatus;
        this.mQueryError = mQueryError;
    }

    public String getmQueryStatus() {
        return mQueryStatus;
    }

    public void setmQueryStatus(String mQueryStatus) {
        this.mQueryStatus = mQueryStatus;
    }

    public String getmQueryError() {
        return mQueryError;
    }

    public void setmQueryError(String mQueryError) {
        this.mQueryError = mQueryError;
    }
}
