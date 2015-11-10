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
import com.mtm.ksu.mobile.model.Parameter;

public class ParameterAdapter extends ArrayAdapter<Parameter>  {
	private List<Parameter> items;
	private List<Parameter> searchItems;
	private ParameterFilter filter;

	public ParameterAdapter(Context context, List<Parameter> items) {
		super(context, R.layout.parameter_item, items);
		this.items = items;
		searchItems = new ArrayList<Parameter>();
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
	            v = li.inflate(R.layout.parameter_item, null);            
	        }
	         
	        Parameter parameter = items.get(position);
	        
	        if(parameter != null) {
	        	TextView parameterNameText = (TextView)v.findViewById(R.id.parameter_name);
	        	//TextView parameterValueText = (TextView)v.findViewById(R.id.parameter_value);
	        	TextView parameterIdText = (TextView)v.findViewById(R.id.parameter_id);
	        	
	        	parameterNameText.setText(" - " + parameter.getName());
	        	//parameterValueText.setText(" - " + parameter.getValue());
	        	parameterIdText.setText(String.valueOf(parameter.getId()));
	        	
	        }
	        
	        
	        
			return v;
	 }
	 
	 
	 
	 @Override
	public android.widget.Filter getFilter() {
		 if (filter == null){
			    filter  = new ParameterFilter();
			   }
		 return filter;
	}


	
	 
	private class ParameterFilter extends android.widget.Filter
	  {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
		    FilterResults result = new FilterResults();
		    if(constraint != null && constraint.toString().length() > 0)
		    {
		    ArrayList<Parameter> filteredItems = new ArrayList<Parameter>();
		 
		    for(int i = 0, l = searchItems.size(); i < l; i++)
		    {
		     Parameter parameter = searchItems.get(i);
		     if(parameter.toString().toLowerCase().contains(constraint))
		      filteredItems.add(parameter);
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
			items = (ArrayList<Parameter>)results.values;
		    notifyDataSetChanged();
		    clear();
		    for(int i = 0, l = items.size(); i < l; i++)
		     add(items.get(i));
		    notifyDataSetInvalidated();
			
		}
		 
	  }
	 
}
