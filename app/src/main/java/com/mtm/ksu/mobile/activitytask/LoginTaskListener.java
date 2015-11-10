package com.mtm.ksu.mobile.activitytask;

import com.mtm.ksu.mobile.model.Login;



public interface LoginTaskListener {
	public void onFetchComplete(Login data);
    public void onFetchFailure(String msg);
}
