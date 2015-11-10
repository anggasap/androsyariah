package com.mtm.ksu.mobile.activity;



import com.mtm.ksu.mobile.model.Parameter;
import com.mtm.ksu.mobile.persistence.DBAdapter;
import com.mtm.ksu.mobile.persistence.LocalParameterRepository;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class ParameterEditActivity extends Activity implements OnClickListener{
	final Context context = this;
	private EditText parameterValueText;
	private TextView parameterNameLbl;
	private Button simpanParameterBtn, cancelParameterBtn;
	private String parameterIdStr;
	private String parameterName;
	private String parameterValue;
	DBAdapter dbAdapter;
	
	ProgressDialog progressDialog ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parameter_form);
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			parameterIdStr = extras.getString("ParameterId");
			parameterName = extras.getString("ParameterName");
			parameterValue = extras.getString("ParameterValue");
			
		}
		
		parameterNameLbl = (TextView)findViewById(R.id.lbl_parameter_name);
		parameterValueText = (EditText)findViewById(R.id.txt_parameter_value);
		
		parameterNameLbl.setText(parameterName);
		parameterValueText.setText(parameterValue);
		
		simpanParameterBtn = (Button) findViewById(R.id.btn_update_editparameter);
		cancelParameterBtn = (Button) findViewById(R.id.btn_cancel_editparameter);
		
		simpanParameterBtn.setOnClickListener(this);
		cancelParameterBtn.setOnClickListener(this);
		
		dbAdapter = new DBAdapter(this);
		
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = null;
		
		if(v == simpanParameterBtn){
			Parameter parameter = new Parameter();
			parameter.setId(Integer.parseInt(parameterIdStr));
			parameter.setName(parameterName);
			parameter.setValue(parameterValueText.getText().toString());
			String message = "Parameter Berhasil Di-update !";
			LocalParameterRepository paramRepo = new LocalParameterRepository(dbAdapter);
			if(paramRepo.updateParameter(parameter)){
				new AlertDialog.Builder(context).setTitle("Andromeda").setMessage(message)
			    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
				Intent intentParameterList = new Intent(getApplicationContext(), ParameterListActivity.class);
				startActivity(intentParameterList); 
				
				
			}else{
				message = "Parameter Gagal Di-Update!";
				new AlertDialog.Builder(context).setTitle("Andromeda").setMessage(message)
			    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
				
			}
			
			
			
		}else if (v == cancelParameterBtn){
			//back to main menu activity
			intent = new Intent(v.getContext(), ParameterListActivity.class);
			startActivity(intent);
		}
		
		
	}
	
	
		
}
