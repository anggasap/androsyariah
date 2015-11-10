package com.mtm.ksu.mobile.activity;




import java.text.SimpleDateFormat;
import java.util.Calendar;


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

public class MainMenuActivity extends ListActivity {
	
	private ListView menuList;
	private String[] menu;
	private String[] code;
	private Integer selectedItem;
	
	SharedPreferencesManager SharedPrefMgr;
	//SharedPreferencesManager SharedPrefMgr;
	
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
		
		
		
	    menu = getResources().getStringArray(R.array.menu_array);
	    code = getResources().getStringArray(R.array.menu_code);
	    setListAdapter(new MenuCustomAdapter(MainMenuActivity.this, R.layout.menu_item, menu));
	    menuList.setOnItemClickListener(new OnItemClickListener() {
	   	    @Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				 selectedItem = ((Long) id).intValue();
	    		   switch(selectedItem)
	    		   {
		    		   case 0:
	   		   				Intent intentSaldoTabungan = new Intent(getApplicationContext(), SaldoTabunganActivity.class);
			   				startActivity(intentSaldoTabungan);
		   					
	   		   		   break;
   		   		   
	    		   		case 1:
	    		   			Intent intentSetorTabungan = new Intent(getApplicationContext(), SetorTabunganActivity.class);
    		   				startActivity(intentSetorTabungan);
		   					
	    		   		   break;
			   				
	    		   		case 2:
	    		   			Intent intentTarikTabungan = new Intent(getApplicationContext(), TarikTabunganActivity.class);
    		   				startActivity(intentTarikTabungan);
	    		   				break;
	    		   		case 3:
	    		   			Intent intentSetorAngsuran = new Intent(getApplicationContext(), SetorAngsuranActivity.class);
    		   				startActivity(intentSetorAngsuran);
	    		   				break;
	    		   			   
	    		   		case 4:
	    		   			Intent intentTransLogList = new Intent(getApplicationContext(), TransLogActivity.class);
    		   				startActivity(intentTransLogList);
	   				
    		   				break;
	    		   		
	    		   		case 5 :
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
					icon.setImageResource(R.drawable.cek_saldo_tab);
					break;
				case 1:
					icon.setImageResource(R.drawable.trans_setor_tab);
					break;
				case 2:
			 		icon.setImageResource(R.drawable.trans_tarik_tab);
			 		break;
				case 3:
					icon.setImageResource(R.drawable.trans_angsuran);
					break;
			 	
			 	case 4:
			 		icon.setImageResource(R.drawable.histori_trans);
			 		break;
			 
			 	case 5:
			 		icon.setImageResource(R.drawable.logout);
			 	    break;
			 	 		
			}
			 return row;
		}
		
	}//end class adapter
}
