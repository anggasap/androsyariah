package com.mtm.ksu.mobile.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.mtm.ksu.mobile.model.Parameter;

public class LocalParameterRepository {

	private DBAdapter adapter;
	public LocalParameterRepository(DBAdapter dbAdapter){
		this.adapter = dbAdapter;
	}
	
	public String getAdminLocalPassword(String name){
		   String passwd = null;
			adapter.open();
	        Cursor c = adapter.getParameterByName(name);
	        if (c.moveToFirst())  {
	        	passwd = c.getString(2);
	        }
	        
	        adapter.close();
	        
	        return passwd;
	}
	
	public boolean updateParameter(Parameter parameter){
		boolean returnValue = false;
		adapter.open();
		returnValue = adapter.updateParameter(parameter.getId(), parameter.getName(), parameter.getValue());
		adapter.close();
		return returnValue;
	}
	
	public Parameter getParameterByName(String name){
		Parameter param = new Parameter();
		adapter.open();
        Cursor c = adapter.getParameterByName(name);
        if (c.moveToFirst())  {
        	param.setId(Integer.parseInt(c.getString(0)));
        	param.setName(c.getString(1));
        	param.setValue(c.getString(2));
        }
        
        adapter.close();
        
        return param;
	}
	
	
	public List<Parameter> getAllParameter(){
		List<Parameter> paramList = new ArrayList<Parameter>();
		adapter.open();
        Cursor c = adapter.getAllParameters();
        if (c.moveToFirst())
        {
            do {
            	Parameter param = new Parameter();
            	param.setId(Integer.parseInt(c.getString(0)));
            	param.setName(c.getString(1));
            	param.setValue(c.getString(2));
            	
            	paramList.add(param);
            } while (c.moveToNext());
        }
        adapter.close();
        
		return paramList;
	}
}
