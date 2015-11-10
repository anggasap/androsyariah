package com.mtm.ksu.mobile.activitytask;

import java.util.ArrayList;
import java.util.List;

import com.mtm.ksu.mobile.activity.R;
import com.mtm.ksu.mobile.model.Transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TransLogAdapter extends ArrayAdapter<Transaction> {
	private List<Transaction> items;
	private List<Transaction> searchItems;
	private TransactionFilter filter;

	public TransLogAdapter(Context context, List<Transaction> items) {
		super(context, R.layout.translog_item, items);
		this.items = items;
		searchItems = new ArrayList<Transaction>();
		this.searchItems.addAll(items);
	}

	@Override
	public int getCount() {
		return items.size();
	}
	
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        if(v == null) {
	            LayoutInflater li = LayoutInflater.from(getContext());
	            v = li.inflate(R.layout.translog_item, null);            
	        }
	         
	        Transaction transaction = items.get(position);
	        
	        if(transaction != null) {
	        	//TextView noRekeningText = (TextView)v.findViewById(R.id.transaksi_no_rekening);
	        	//noRekeningText.setVisibility(View.INVISIBLE);
	        	TextView nasabahText = (TextView)v.findViewById(R.id.translog_nama_nasabah);
	        	TextView tipeTransText = (TextView)v.findViewById(R.id.translog_tipe_trans);
	        	TextView nilaiTransText = (TextView)v.findViewById(R.id.translog_nominal_trans);
	        	TextView transIdText = (TextView)v.findViewById(R.id.translog_trans_id);
	        	
	        	//noRekeningText.setText(transaction.getNoRekeningKredit());
	        	nasabahText.setText(transaction.getNamaNasabah());
	        	String tipeTrans = "";
	        	if("SETOR_TABUNGAN".equals(transaction.getTransType())){
	        		tipeTrans = "SETOR-TAB";
	        	}else if ("TARIK_TABUNGAN".equals(transaction.getTransType())){
	        		tipeTrans = "TARIK-TAB";
	        	}else if ("ANGSURAN".equals(transaction.getTransType())){
	        		tipeTrans = "ANGS-KRE";
	        	}
	        	tipeTransText.setText(" - " +tipeTrans);
	        	nilaiTransText.setText(" - " + transaction.getJumlahTrans());
	        	transIdText.setText(transaction.getTransLogId());
	        	
	        }
	        
	        
	        
			return v;
	 }
	 @Override
		public android.widget.Filter getFilter() {
			 if (filter == null){
				    filter  = new TransactionFilter();
				   }
			 return filter;
		}
	 
	 private class TransactionFilter extends android.widget.Filter
	  {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
		    FilterResults result = new FilterResults();
		    if(constraint != null && constraint.toString().length() > 0)
		    {
		    ArrayList<Transaction> filteredItems = new ArrayList<Transaction>();
		 
		    for(int i = 0, l = searchItems.size(); i < l; i++)
		    {
		    	Transaction transaction = searchItems.get(i);
		     if(transaction.toString().toLowerCase().contains(constraint))
		      filteredItems.add(transaction);
		    }
		    result.count = filteredItems.size();
		    result.values = filteredItems;
		    }else
		    {
		        synchronized(this)
		        {
		         result.values = items;
		         result.count = items.size();
		        }
		       }
		       return result;
		
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			items = (ArrayList<Transaction>)results.values;
		    notifyDataSetChanged();
		    clear();
		    for(int i = 0, l = items.size(); i < l; i++)
		     add(items.get(i));
		    notifyDataSetInvalidated();
			
		}
		 
	  }
}
