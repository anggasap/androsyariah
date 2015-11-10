package com.mtm.ksu.mobile.activity;




import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.model.Parameter;
import com.mtm.ksu.mobile.persistence.DBAdapter;
import com.mtm.ksu.mobile.persistence.LocalParameterRepository;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AdminMenuActivity extends ListActivity {
	
	private ListView menuList;
	private String[] menu;
	private String[] code;
	private Integer selectedItem;
	
	SharedPreferencesManager SharedPrefMgr;
	DBAdapter dbAdapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPrefMgr = new SharedPreferencesManager(this);
		
		TextView infoLbl = new TextView(this);
		//String tglOpr =SharedPrefMgr.getTanggalOperasional();
		//String tglOprFmt = tglOpr.substring(6,8)+"-"+tglOpr.substring(4,6) +"-"+tglOpr.substring(0,4);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
		infoLbl.setText("Tanggal : " + formattedDate);
		
		getListView().addHeaderView(infoLbl);
		
		menuList = getListView();
		menuList.setTextFilterEnabled(true);
		
		dbAdapter = new DBAdapter(this);
		
		
	    menu = getResources().getStringArray(R.array.menu_admin_array);
	    code = getResources().getStringArray(R.array.menu_admin_code);
	    setListAdapter(new MenuCustomAdapter(AdminMenuActivity.this, R.layout.menu_item, menu));
	    menuList.setOnItemClickListener(new OnItemClickListener() {
	   	    @Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				 selectedItem = ((Long) id).intValue();
	    		   switch(selectedItem)
	    		   {
	    		   		case 0:
	    		   			    LocalParameterRepository paramRepo = new LocalParameterRepository(dbAdapter);
	    		   			    Parameter parameter = paramRepo.getParameterByName(Constants.ANDROMEDA_KEYNAME_LOCAL_ADMIN_PASSWORD);
		    		   			Intent parameterEditIntent = new Intent(getApplicationContext(),ParameterEditActivity.class);
		    		   			
						 		parameterEditIntent.putExtra("ParameterId", String.valueOf(parameter.getId()));
						 		parameterEditIntent.putExtra("ParameterName", parameter.getName());
						 		parameterEditIntent.putExtra("ParameterValue", parameter.getValue());
						 		
						 		startActivity(parameterEditIntent);
	    		   				
						 		break;
	    		   				
	    		   		case 1:
	    		   			Intent intentParameterList = new Intent(getApplicationContext(), ParameterListActivity.class);
		   					startActivity(intentParameterList);
		   				
    		   				break;
		   				
	    		   		case 2 :
	    		   			   Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
	    		   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		   			   SharedPrefMgr.clear();
	    		   			   startActivity(loginIntent);
	    		   			   finish();
	    		   			   break;
	    		   }
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		//do nothing. prevent back button pressed
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		createMenu(menu);
		return true;
	}

	private void createMenu(Menu menu) {
		MenuItem exitMenu = menu.add(0,0,0, "Exit");
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuChoice(item);
	}

	private boolean menuChoice(MenuItem item) {
			switch (item.getItemId()) {
			case 0:
				   Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
	   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   			   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   			   startActivity(loginIntent);
	   			   finish();
					return true;

			}
			
		return false;
	}

	public class  MenuCustomAdapter extends ArrayAdapter<String> {
		public MenuCustomAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}
		
		// Layouting
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.menu_item, parent, false);
			TextView label = (TextView)row.findViewById(R.id.list_menu);
			label.setText(menu[position]);
			ImageView icon = (ImageView)row.findViewById(R.id.list_icon);

			switch (position) {
			 	case 0:
			 		icon.setImageResource(R.drawable.change_password);
			 		break;
			 	case 1:
			 		icon.setImageResource(R.drawable.settings);
			 		break;
			 
			 	case 2:
			 		icon.setImageResource(R.drawable.logout);
			 	    break;
			 	 		
			}
			 return row;
		}
		
	}//end class adapter
}
