package com.mtm.ksu.mobile.activity;



import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mtm.ksu.mobile.activitytask.LoginTask;
import com.mtm.ksu.mobile.activitytask.LoginTaskListener;
import com.mtm.ksu.mobile.common.ActionConstants;
import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.model.Login;
import com.mtm.ksu.mobile.model.Parameter;
import com.mtm.ksu.mobile.persistence.DBAdapter;
import com.mtm.ksu.mobile.persistence.LocalParameterRepository;
import com.mtm.ksu.mobile.request.LoginRequest;


public class LoginActivity extends Activity {
	final Context context = this;
	EditText userNameText,userPasswordText;
	Button loginBtn,cancelBtn;
	ProgressDialog progressDialog ;
	AlertDialog alertDialog;
	
	
	SharedPreferencesManager SharedPrefMgr;
	DBAdapter dbAdapter ;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        SharedPrefMgr = new SharedPreferencesManager(this);
        
        userNameText = (EditText)findViewById(R.id.user_name_login);
        userPasswordText = (EditText)findViewById(R.id.user_password_login);
        loginBtn = (Button)findViewById(R.id.btn_login);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        
        LoginListener loginListener = new LoginListener();
        loginBtn.setOnClickListener(loginListener);
        cancelBtn.setOnClickListener(loginListener);
        dbAdapter = new DBAdapter(this);
        
       
    }
	
	private boolean validate(){
		boolean result = false;
		
		if(	!"".equals(userNameText.getText().toString()) && 
				  !"".equals(userPasswordText.getText().toString())){
			result =  true;
			
		}
		
		
		return result;
	}
	
	
	
	private class LoginListener implements OnClickListener, LoginTaskListener{
		String userName;
		String password;
		
		@Override
		public void onClick(View v) {
			
			if(v ==loginBtn){
				if(validate()){
					userName = userNameText.getText().toString().toUpperCase();
					password = userPasswordText.getText().toString();
					if(Constants.ANDROMEDA_LOCAL_ADMIN_USER.equals(userName)){
					  //validasi ke sql-lite local
						 try{
						
							LocalParameterRepository localRepo = new LocalParameterRepository(dbAdapter);
							String localPassword = localRepo.getAdminLocalPassword(Constants.ANDROMEDA_KEYNAME_LOCAL_ADMIN_PASSWORD);
							if(localPassword.equals(password)){
								Intent mainMenuIntent = new Intent(context,AdminMenuActivity.class);
			    				startActivity(mainMenuIntent);
								
							}else{
								String message = "Password Admin Salah !";
			    				new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(message)
			    			    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
			    					@Override
			    					public void onClick(DialogInterface dialog, int which) {
			    						dialog.cancel();
			    					}
			    				}).show();
								
							}
							
						}catch(Exception e){
							String message = "SQLite Error !";
							new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(message)
		    			    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
		    					@Override
		    					public void onClick(DialogInterface dialog, int which) {
		    						dialog.cancel();
		    					}
		    				}).show();
						}
						
						
					}else{
						//update value dari Constants tertentu (mis : server url)
						initializeConstants();
						progressDialog = new ProgressDialog(context);
						progressDialog.setMessage("Loading ....");
						progressDialog.show();
						
					    //validasi ke server andromeda
						String URLLoginAuthenticateAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.LOGIN_SERVICE_AUTHENTICATE;
						
						LoginTask task = new LoginTask(this);
						LoginRequest request = new LoginRequest();
						request.setUserName(userName);
						request.setPassword(password);
						
						task.setRequest(request);
					    task.execute(URLLoginAuthenticateAction);
						
					}
				}else{
					String message = "User Name dan Password Tidak Boleh Kosong !";
    				new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(message)
    			    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
    					@Override
    					public void onClick(DialogInterface dialog, int which) {
    						dialog.cancel();
    					}
    				}).show();
				}
				
			}else if(v == cancelBtn){
								
				finish();
				
			}
			
		}
		
		private void  initializeConstants(){
			LocalParameterRepository paramRepo = new LocalParameterRepository(dbAdapter);
			Parameter parameter = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_SERVER_ADDRESS);
			Constants.HTTP_SERVER_SERVICE = parameter.getValue().trim();
   			
		}

		@Override
		public void onFetchComplete(Login data) {
			if(progressDialog != null) progressDialog.dismiss();
			LocalParameterRepository paramRepo = new LocalParameterRepository(dbAdapter);
			Parameter kodeTransSetor = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_KODETRANSSETOR);
			Parameter kodeTransTarik = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_KODETRANSTARIK);
			Parameter kodeTransAngsuran = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_KODETRANSANGSURAN);
			Parameter companyName = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_COMPANY_NAME);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = df.format(c.getTime());
	        
	        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
	        String imei = mngr.getDeviceId();
			
			SharedPrefMgr.setUserId(data.getUserId());
			SharedPrefMgr.setKodeTransSetor(kodeTransSetor.getValue());
			SharedPrefMgr.setKodeTransTarik(kodeTransTarik.getValue());
			SharedPrefMgr.setKodeTransAngsuran(kodeTransAngsuran.getValue());
			SharedPrefMgr.setTanggalOperasional(formattedDate);
			SharedPrefMgr.setCompanyName(companyName.getValue());
			SharedPrefMgr.setImei(imei);
			
			Intent mainMenuIntent = new Intent(context,MainMenuActivity.class);
			startActivity(mainMenuIntent);
			
		}

		@Override
		public void onFetchFailure(String msg) {
			if(progressDialog != null) progressDialog.dismiss();
			new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(msg)
		    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).show();
			
		}
		
		
	}



	
	
}
