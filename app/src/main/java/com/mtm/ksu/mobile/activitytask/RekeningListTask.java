package com.mtm.ksu.mobile.activitytask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.common.JsonResponseUtility;
import com.mtm.ksu.mobile.model.Rekening;
import com.mtm.ksu.mobile.request.ParameterRequest;
import com.mtm.ksu.mobile.service.CustomHttpClient;



import android.os.AsyncTask;

public class RekeningListTask extends AsyncTask<String, Void, String>{
	private final RekeningListTaskListener listener;
	private ParameterRequest request;
    private String msg;
     
    public RekeningListTask(RekeningListTaskListener listener) {
        this.listener = listener;
    }
    
    public void setRequest(ParameterRequest request){
    	this.request = request;
    }
	@Override
	protected String doInBackground(String... params) {
		String output = null;
		if(params == null) return null;
		String url = params[0];
		 try {
			 if(request != null){
				 if(request.getKriteria() != null){
					 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					 postParameters.add(new BasicNameValuePair("Name", request.getKriteria()));
					 output = CustomHttpClient.executeHttpPost(url, postParameters);
				 }
				 
			 }else{
				 output = CustomHttpClient.executeHttpGet(url);
			 }
					 
			 
		 }catch(IOException e){
			 
		 } catch (Exception e) {
			//e.printStackTrace();
		 }
		
		return output;
	}
	
	@Override
    protected void onPostExecute(String sJson) {
		JsonResponseUtility _JsonResponse = new JsonResponseUtility(sJson);
		List<Rekening> rekeningList = new ArrayList<Rekening>();
		if(Constants.SERVER_SUCCESS_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			try {
				JSONArray _jsonArray = _JsonResponse.getResponseList();
				for(int i = 0; i<_jsonArray.length();i++){
					   JSONObject jsonChildNode = _jsonArray.getJSONObject(i);
					   Rekening rekening = new Rekening();
					   rekening.setNoRekening(jsonChildNode.optString("NO_REKENING").trim());
					   rekening.setNamaNasabah(jsonChildNode.optString("NAMA_NASABAH").trim());
					   if(jsonChildNode.optString("SALDO_AKHIR") != ""){
						   rekening.setSaldo(jsonChildNode.optString("SALDO_AKHIR").trim());
					   }
					  
					   rekeningList.add(rekening);
				}
				
				//notify the activity that fetch data has been complete
	            if(listener != null) listener.onFetchComplete(rekeningList);
			} catch (JSONException e) {
				msg = "Invalid response";
	            if(listener != null) listener.onFetchFailure(msg);
	            return;
				
			}
		
		}else if(Constants.SERVER_EMPTY_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			//empty rekening list
			 if(listener != null) listener.onFetchComplete(rekeningList);
		
		}else if(Constants.SERVER_ERROR_RESPONSE.equals(_JsonResponse.getResponseStatus()) || Constants.SERVER_INVALID_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			JSONObject _jsonObj = _JsonResponse.getResponseDetail();
			try {
				msg = _jsonObj.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  if(listener != null) listener.onFetchFailure(msg);
            return;
		}else{
			msg = "Sistem sedang mengalami gangguan !";
			if(listener != null) listener.onFetchFailure("");
            return;
		}
	}
}
