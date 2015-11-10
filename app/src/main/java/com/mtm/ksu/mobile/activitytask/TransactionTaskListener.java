package com.mtm.ksu.mobile.activitytask;

import java.util.List;

import com.mtm.ksu.mobile.model.Rekening;
import com.mtm.ksu.mobile.model.Transaction;


public interface TransactionTaskListener {
	public void onTransactionComplete(Transaction data);
    public void onTransactionFailure(String msg);
}
