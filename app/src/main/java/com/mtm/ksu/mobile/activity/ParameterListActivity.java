package com.mtm.ksu.mobile.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mtm.ksu.mobile.activitytask.ParameterAdapter;
import com.mtm.ksu.mobile.model.Parameter;
import com.mtm.ksu.mobile.persistence.DBAdapter;
import com.mtm.ksu.mobile.persistence.LocalParameterRepository;


public class ParameterListActivity extends ListActivity  {
	private ProgressDialog dialog;
	SharedPreferencesManager SharedPrefMgr;
	private AlertDialog infoDialog;
	
	protected static final int CONTEXTMENU_OPTION_EDIT = 1; 
	protected static final int CONTEXTMENU_OPTION_TRANSACTION = 2; 
	
	private DialogInterface.OnClickListener closeListener;
	ListView listView;
	ParameterAdapter adapter ;
	Map<String, Parameter> parameterMap = new HashMap<String, Parameter>();
	DBAdapter dbAdapter ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPrefMgr = new SharedPreferencesManager(this);
		//final LayoutParams lparams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	       
		EditText inputSearch = new EditText(this);
		inputSearch.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		inputSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_button, 0, 0, 0);
		//inputSearch.setLayoutParams(new LayoutParams(arg0) );
		inputSearch.addTextChangedListener(new TextWatcher() {


	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text
	        	adapter.getFilter().filter(cs.toString().toUpperCase()); 
	        }


	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                int arg3) {
	            // TODO Auto-generated method stub

	        }


	        public void afterTextChanged(Editable arg0) {
	            // TODO Auto-generated method stub                          
	        }
	    });     
		
	    getListView().addHeaderView(inputSearch);
	    getListView().setTextFilterEnabled(true);
		
	    dbAdapter = new DBAdapter(this);
	   
	    
		initView();
		
		listView = getListView();
		registerForContextMenu(listView);
		//listView.setTextFilterEnabled(true);
		
		
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			 menu.setHeaderTitle("Action");
			 menu.add(Menu.NONE, CONTEXTMENU_OPTION_EDIT, 0, "Edit"); 
			 //menu.add(Menu.NONE, CONTEXTMENU_OPTION_TRANSACTION, 1, "Pembayaran"); 
			 
			 super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onBackPressed() {
		//do nothing. prevent back button pressed;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
			menu.add(Menu.NONE,Menu.FIRST,Menu.NONE, "Menu Utama");
			menu.add(Menu.NONE,Menu.FIRST + 1,Menu.NONE, "Exit");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case 1:
			   Intent mainMenuIntent = new Intent(getApplicationContext(), AdminMenuActivity.class);
			   startActivity(mainMenuIntent);
			   return true;
			   
			   
		case 2:
			  
			   Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   			   SharedPrefMgr.clear();
   			   startActivity(loginIntent);
   			   finish();
			   return true;

		}
		
	return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		if(menuInfo.targetView instanceof LinearLayout){
				LinearLayout linearLayoutParent = (LinearLayout) menuInfo.targetView;
				TextView parameterIdTxt = (TextView) linearLayoutParent.getChildAt(0);
				Parameter parameter = parameterMap.get(parameterIdTxt.getText().toString());
			
				switch (item.getItemId()) {
				 	case CONTEXTMENU_OPTION_EDIT:
				 		Intent parameterEditIntent = new Intent(this,ParameterEditActivity.class);
				 		parameterEditIntent.putExtra("ParameterId", parameterIdTxt.getText().toString());
				 		parameterEditIntent.putExtra("ParameterName", parameter.getName());
				 		parameterEditIntent.putExtra("ParameterValue", parameter.getValue());
				 		startActivity(parameterEditIntent);
				 		break;
				 	
				 	case CONTEXTMENU_OPTION_TRANSACTION:
				 		/*
				 		Intent pembayaranTagihanIntent = new Intent(this,TransaksiPembayaranActivity.class);
				 		pembayaranTagihanIntent.putExtra("NoRekening", noRekeningTxt.getText().toString());
				 		pembayaranTagihanIntent.putExtra("NoRekeningDca", tagihan.getNoRekeningDCA());
				 		pembayaranTagihanIntent.putExtra("NamaNasabah", tagihan.getNamaNasabah());
				 		startActivity(pembayaranTagihanIntent);
				 		*/
				 		break;
				}
		
		}
		return true;
	}


	private void initView() {
		dialog = ProgressDialog.show(this, "", "Loading...");
		
		
		LocalParameterRepository paramRepo = new LocalParameterRepository(dbAdapter);
		List<Parameter> data = paramRepo.getAllParameter();
		
        // create new adapter
         adapter = new ParameterAdapter(this, data);
        // set the adapter to list
        setListAdapter(adapter);
        
        //populate data on Map
        Iterator<Parameter> it = data.iterator();
        while(it.hasNext()){
        	Parameter parameter = it.next();
        	parameterMap.put(String.valueOf(parameter.getId()), parameter);
        }
        
        if(dialog != null)  dialog.dismiss();
        
       // registerForContextMenu(listView);
		
	}
	
	
	

	
}
