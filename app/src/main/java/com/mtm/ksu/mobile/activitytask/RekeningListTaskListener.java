package com.mtm.ksu.mobile.activitytask;

import java.util.List;

import com.mtm.ksu.mobile.model.Rekening;


public interface RekeningListTaskListener {
	public void onFetchComplete(List<Rekening> data);
    public void onFetchFailure(String msg);
}
