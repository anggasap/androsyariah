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
import com.mtm.ksu.mobile.model.Transaction;
import com.mtm.ksu.mobile.request.TransactionRequest;
import com.mtm.ksu.mobile.service.CustomHttpClient;

import android.os.AsyncTask;


public class TransLogListTask extends AsyncTask<String, Void, String>{
	private final TransLogListListener listener;
	private TransactionRequest request;
    private String msg;
     
    public TransLogListTask(TransLogListListener listener) {
        this.listener = listener;
    }
    
    public void setRequest(TransactionRequest request){
    	this.request = request;
    }
	@Override
	protected String doInBackground(String... params) {
		String output = null;
		if(params == null) return null;
		String url = params[0];
		 try {
			 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			 postParameters.add(new BasicNameValuePair("TanggalTrans", request.getTanggalTrans()));
			 postParameters.add(new BasicNameValuePair("IMEI", request.getImei()));
			
			 output = CustomHttpClient.executeHttpPost(url, postParameters);
			 
		 }catch(IOException e){
			 
		 } catch (Exception e) {
			//e.printStackTrace();
		 }
		
		return output;
	}
	
	@Override
    protected void onPostExecute(String sJson) {
		JsonResponseUtility _JsonResponse = new JsonResponseUtility(sJson);
		List<Transaction> transactionList = new ArrayList<Transaction>();
		if(Constants.SERVER_SUCCESS_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			try {
				JSONArray _jsonArray = _JsonResponse.getResponseList();
				for(int i = 0; i<_jsonArray.length();i++){
					   JSONObject jsonChildNode = _jsonArray.getJSONObject(i);
					   Transaction trx = new Transaction();
					   trx.setTransLogId(jsonChildNode.optString("mobiletrans_id"));
					   trx.setTransType(jsonChildNode.optString("mobiletrans_type"));
					   trx.setNoRekening(jsonChildNode.optString("no_rekening"));
					   trx.setNamaNasabah(jsonChildNode.optString("nama_nasabah").trim());
					   trx.setNoKuitansi(jsonChildNode.optString("no_kuitansi"));
					   trx.setJumlahTrans(jsonChildNode.optString("nominal_trans"));
					   
					   transactionList.add(trx);
				}
				
				//notify the activity that fetch data has been complete
	            if(listener != null) listener.onFetchComplete(transactionList);
			} catch (JSONException e) {
				msg = "Invalid response";
	            if(listener != null) listener.onFetchFailure(msg);
	            return;
				
			}
		}else if(Constants.SERVER_EMPTY_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			//empty rekening list
			 if(listener != null) listener.onFetchComplete(transactionList);
		
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
