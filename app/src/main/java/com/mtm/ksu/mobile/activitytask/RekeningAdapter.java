package com.mtm.ksu.mobile.activitytask;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mtm.ksu.mobile.activity.R;
import com.mtm.ksu.mobile.model.Rekening;

public class RekeningAdapter extends ArrayAdapter<Rekening>  {
	private List<Rekening> items;
	private List<Rekening> searchItems;
	private RekeningFilter filter;

	public RekeningAdapter(Context context, List<Rekening> items) {
		super(context, R.layout.rekening_item, items);
		this.items = items;
		searchItems = new ArrayList<Rekening>();
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
	            v = li.inflate(R.layout.rekening_item, null);            
	        }
	         
	        Rekening rekening = items.get(position);
	        
	        if(rekening != null) {
	        	TextView noRekeningText = (TextView)v.findViewById(R.id.no_rekening_item);
	        	TextView namaNasabahText = (TextView)v.findViewById(R.id.nama_nasabah_item);
	        	
	        	noRekeningText.setText(rekening.getNoRekening());
	        	namaNasabahText.setText(" - "+rekening.getNamaNasabah());
	        	
	        }
	        
	        
	        
			return v;
	 }
	 
	 
	 
	 @Override
	public android.widget.Filter getFilter() {
		 if (filter == null){
			    filter  = new RekeningFilter();
			   }
		 return filter;
	}


	
	 
	private class RekeningFilter extends android.widget.Filter
	  {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
		    FilterResults result = new FilterResults();
		    if(constraint != null && constraint.toString().length() > 0)
		    {
		    ArrayList<Rekening> filteredItems = new ArrayList<Rekening>();
		 
		    for(int i = 0, l = searchItems.size(); i < l; i++)
		    {
		     Rekening rekening = searchItems.get(i);
		     if(rekening.toString().toLowerCase().contains(constraint))
		      filteredItems.add(rekening);
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
			items = (ArrayList<Rekening>)results.values;
		    notifyDataSetChanged();
		    clear();
		    for(int i = 0, l = items.size(); i < l; i++)
		     add(items.get(i));
		    notifyDataSetInvalidated();
			
		}
		 
	  }
	 
}
