package com.mtm.ksu.mobile.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonResponseUtility {
	private JSONObject jsonObject ;
	private String responseStatus;
	private JSONObject responseDetail;
	
	public JsonResponseUtility(String jsonString){
		try {
			
			jsonObject = new JSONObject(jsonString);
			responseStatus = jsonObject.getString("ResponseStatus");
			responseDetail = jsonObject.getJSONObject("ResponseDetail");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public String getResponseStatus(){
		return responseStatus;
	}
	
	public JSONObject getResponseDetail(){
		return responseDetail;
	}
	
	public JSONObject getJsonObject(){
		return jsonObject;
	}
	
	public JSONArray getResponseList(){
		return getJsonObject().optJSONArray("ResponseDetail");
	}
	
}
