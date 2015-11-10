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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtm.ksu.mobile.activitytask.RekeningAdapter;
import com.mtm.ksu.mobile.activitytask.RekeningListTaskListener;
import com.mtm.ksu.mobile.activitytask.RekeningListTask;
import com.mtm.ksu.mobile.common.ActionConstants;
import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.model.Rekening;


public class RekeningListActivity extends ListActivity implements RekeningListTaskListener {
	private ProgressDialog dialog;
	SharedPreferencesManager SharedPrefMgr;
	private AlertDialog infoDialog;
	
	protected static final int CONTEXTMENU_OPTION_INFO = 1; 
	protected static final int CONTEXTMENU_OPTION_TRANSACTION = 2; 
	
	private DialogInterface.OnClickListener closeListener;
	ListView listView;
	RekeningAdapter adapter ;
	Map<String, Rekening> rekeningMap = new HashMap<String, Rekening>();
	
	
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
		
		initView();
		
		listView = getListView();
		//listView.setTextFilterEnabled(true);
		
		
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			 menu.setHeaderTitle("Action");
			 menu.add(Menu.NONE, CONTEXTMENU_OPTION_INFO, 0, "Info"); 
			 menu.add(Menu.NONE, CONTEXTMENU_OPTION_TRANSACTION, 1, "Pembayaran"); 
			 
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
			   Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
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
				TextView noRekeningTxt = (TextView) linearLayoutParent.getChildAt(0);
				Rekening rekening = rekeningMap.get(noRekeningTxt.getText().toString());
			
				switch (item.getItemId()) {
				 	case CONTEXTMENU_OPTION_INFO:
				 		LayoutInflater factory = LayoutInflater.from(RekeningListActivity.this);
				 		infoDialog = new AlertDialog.Builder(RekeningListActivity.this).create();
				 		final View infoView = factory.inflate(
								R.layout.rekening_information, null);
				 		/*
				 		final TextView infoNoRekeningText, noRekeningDcaText, namaNasabahText, tagihanPokokText, tagihanBungaText, tagihanTotalText, saldoDcaText;
				 		final TextView tagihanHarianText, tanggalJatuhTempoText;
				 		
				 		infoNoRekeningText = (TextView)infoView.findViewById(R.id.tgh_no_rekening);
				        noRekeningDcaText = (TextView)infoView.findViewById(R.id.tgh_no_rekening_dca);
				        namaNasabahText = (TextView)infoView.findViewById(R.id.tgh_nama_nasabah);
				        tagihanPokokText = (TextView)infoView.findViewById(R.id.tgh_tagihan_pokok);
				        tagihanBungaText = (TextView)infoView.findViewById(R.id.tgh_tagihan_bunga);
				        tagihanTotalText = (TextView)infoView.findViewById(R.id.tgh_tagihan_total);
				        saldoDcaText = (TextView)infoView.findViewById(R.id.tgh_saldo_dca);
				        tagihanHarianText = (TextView)infoView.findViewById(R.id.tgh_tagihan_harian);
				        tanggalJatuhTempoText = (TextView)infoView.findViewById(R.id.tgh_tgl_jatuh_tempo);
				        
				        
				        final Button closeBtn = (Button) infoView.findViewById(R.id.btn_tgh_close);
				        closeBtn.setVisibility(View.GONE);
				        
				        double dtagihanpokok = Double.parseDouble(tagihan.getTagihanPokok());
						double dtagihanbunga = Double.parseDouble(tagihan.getTagihanBunga());
						double dtotaltagihan = dtagihanpokok + dtagihanbunga;
						NumberFormat nf = NumberFormat.getInstance();
						
						
						infoNoRekeningText.setText    ("No Rekening      : \n \t\t\t" + tagihan.getNoRekening());
						noRekeningDcaText.setText     ("No Rekening DCA  : \n \t\t\t"+ tagihan.getNoRekeningDCA());
						namaNasabahText.setText       ("Nama Nasabah     : \n \t\t\t"+ tagihan.getNamaNasabah());
						tagihanPokokText.setText      ("Tagihan Pokok    : \n \t\t\t"+ nf.format(Double.parseDouble(tagihan.getTagihanPokok())));
						tagihanBungaText.setText      ("Tagihan Bunga    : \n \t\t\t"+ nf.format(Double.parseDouble(tagihan.getTagihanBunga())));
						tagihanTotalText.setText      ("Total Tagihan    : \n \t\t\t"+ nf.format(dtotaltagihan));
						saldoDcaText.setText          ("Saldo Titipan Angsuran        : \n \t\t\t"+ nf.format(Double.parseDouble(tagihan.getSaldoDCA())));
						tanggalJatuhTempoText.setText ("Tanggal Jatuh Tempo :\n \t\t\t" + tagihan.getTanggalJatuhTempo());
						tagihanHarianText.setText     ("Tagihan Harian : \n \t\t\t" + nf.format(Double.parseDouble(tagihan.getTagihanHarian())));
					
						
						closeListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						};
						
						infoDialog.setView(infoView);
						infoDialog.setTitle("Informasi Tagihan");
						infoDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Tutup",
								closeListener);
						
						infoDialog.show();
						*/
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
		String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_FETCH_DATA_LIST;
		RekeningListTask task = new RekeningListTask(this);
		task.execute(URLFetchDataRekeningListAction);
		
	}
	
	/*
	public void onListItemClick(ListView parent, View view, int position, long id) {
		  	LinearLayout linearLayoutParent = (LinearLayout) view;
		 	TextView noRekeningTxt = (TextView) linearLayoutParent.getChildAt(0);
		 	Intent showTagihanIntent = new Intent(this,ShowTagihanInfoActivity.class);
		 	showTagihanIntent.putExtra("NoRekening", noRekeningTxt.getText().toString());
			startActivity(showTagihanIntent);
			//Toast.makeText(this, "You have selected " + noRekeningTxt.getText().toString(), 
			//Toast.LENGTH_SHORT).show();
	}
	*/
	

	@Override
	public void onFetchComplete(List<Rekening> data) {
		 // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // create new adapter
         adapter = new RekeningAdapter(this, data);
        // set the adapter to list
        setListAdapter(adapter);
        
        //populate data on Map
        Iterator<Rekening> it = data.iterator();
        while(it.hasNext()){
        	Rekening rekening = it.next();
        	rekeningMap.put(rekening.getNoRekening(), rekening);
        }
      
        registerForContextMenu(listView);
	}

	@Override
	public void onFetchFailure(String msg) {
		// dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();  
		
	}
}
