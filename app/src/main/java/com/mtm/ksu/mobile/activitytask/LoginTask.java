package com.mtm.ksu.mobile.activitytask;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.common.JsonResponseUtility;
import com.mtm.ksu.mobile.model.Login;
import com.mtm.ksu.mobile.request.LoginRequest;
import com.mtm.ksu.mobile.service.CustomHttpClient;

public class LoginTask extends AsyncTask<String, Void, String>{
	private final LoginTaskListener listener;
	private LoginRequest request;
    private String msg = null;
     
    public LoginTask(LoginTaskListener listener) {
        this.listener = listener;
    }
    
    public void setRequest(LoginRequest request){
    	this.request = request;
    }
	@Override
	protected String doInBackground(String... params) {
		String output = null;
		if(params == null) return null;
		String url = params[0];
		 try {
			 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			 postParameters.add(new BasicNameValuePair("UserName", request.getUserName()));
 			 postParameters.add(new BasicNameValuePair("Password", request.getPassword()));	
 			
			 output = CustomHttpClient.executeHttpPost(url, postParameters);
			 
		 }catch(IOException e){
			 e.printStackTrace();
		 } catch (Exception e) {
			//e.printStackTrace();
		 }
		
		return output;
	}
	
	@Override
    protected void onPostExecute(String sJson) {
		if(sJson != null){
		
			JsonResponseUtility _JsonResponse = new JsonResponseUtility(sJson);
			if(Constants.SERVER_SUCCESS_RESPONSE.equals(_JsonResponse.getResponseStatus())){
				JSONObject _jsonObj = _JsonResponse.getResponseDetail();
				Login login = new Login();
				
				try{
					login.setUserId(_jsonObj.getString("USERID"));
					login.setValid(true);
					login.setUserName(request.getUserName());
					login.setPassword(request.getPassword());
					}catch(JSONException e){
					
				}
				//notify the activity that fetch data has been complete
		        if(listener != null) listener.onFetchComplete(login);
				
			}else if(Constants.SERVER_INVALID_RESPONSE.equals(_JsonResponse.getResponseStatus())){
				JSONObject _jsonObj = _JsonResponse.getResponseDetail();
				try {
					msg = _jsonObj.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	            if(listener != null) listener.onFetchFailure(msg);
	            return;
			}else{
				JSONObject _jsonObj = _JsonResponse.getResponseDetail();
				try {
					msg = _jsonObj.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				if(listener != null) listener.onFetchFailure(msg);
	            return;
			}
		}else{
			if(listener != null) listener.onFetchFailure("Server tidak merespons atau VPN belum tersambung ! ");
		}
	}
}
