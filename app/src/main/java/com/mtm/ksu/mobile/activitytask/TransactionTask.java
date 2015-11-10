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
import com.mtm.ksu.mobile.model.Transaction;
import com.mtm.ksu.mobile.request.TransactionRequest;
import com.mtm.ksu.mobile.service.CustomHttpClient;

public class TransactionTask extends AsyncTask<String, Void, String>{
	private final TransactionTaskListener listener;
	private TransactionRequest request;
    private String msg;
     
    public TransactionTask(TransactionTaskListener listener) {
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
			 postParameters.add(new BasicNameValuePair("NoRekening", request.getNoRekening()));
			 postParameters.add(new BasicNameValuePair("TanggalTrans", request.getTanggalTrans()));
			 postParameters.add(new BasicNameValuePair("JenisTrans", "Tunai"));
			 postParameters.add(new BasicNameValuePair("JumlahTrans", request.getJumlahTrans()));
             postParameters.add(new BasicNameValuePair("SaldoTabAft", request.getSaldoTabAft()));
			 postParameters.add(new BasicNameValuePair("JumlahPokok", request.getJumlahPokok()));
			 postParameters.add(new BasicNameValuePair("JumlahBunga", request.getJumlahBunga()));
			 postParameters.add(new BasicNameValuePair("JumlahDenda", request.getJumlahDenda()));
             postParameters.add(new BasicNameValuePair("JumlahAdmin", request.getJumlahAdmin()));
             postParameters.add(new BasicNameValuePair("AngsKe", request.getAngsKe()));
             postParameters.add(new BasicNameValuePair("TglJt", request.getTglJt()));
			 postParameters.add(new BasicNameValuePair("KodeTrans", request.getKodeTrans()));
			 postParameters.add(new BasicNameValuePair("UserId", request.getUserId()));
			 postParameters.add(new BasicNameValuePair("IMEI", request.getImei() ));
				
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
		if(Constants.SERVER_SUCCESS_RESPONSE.equals(_JsonResponse.getResponseStatus())){
			try {
				Transaction transaction = new Transaction();
				JSONObject _jsonObj = _JsonResponse.getResponseDetail();
				transaction.setServerResponseMessage(_jsonObj.getString("message"));
				transaction.setNoKuitansi(_jsonObj.getString("no_kuitansi"));
				transaction.setServerResponseStatus(Constants.SERVER_SUCCESS_RESPONSE);
				
				
				//notify the activity that fetch data has been complete
	            if(listener != null) listener.onTransactionComplete(transaction);
			} catch (JSONException e) {
				msg = "Invalid response";
	            if(listener != null) listener.onTransactionFailure(msg);
	            return;
				
			}
		
		
		}else{
			
			JSONObject _jsonObj = _JsonResponse.getResponseDetail();
			try {
				msg = _jsonObj.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  if(listener != null) listener.onTransactionFailure(msg);
            return;
		}
		
	}
}
