package com.mtm.ksu.mobile.activitytask;

import java.util.List;

import com.mtm.ksu.mobile.model.Transaction;


public interface TransLogListListener {
	public void onFetchComplete(List<Transaction> data);
    public void onFetchFailure(String msg);
}
