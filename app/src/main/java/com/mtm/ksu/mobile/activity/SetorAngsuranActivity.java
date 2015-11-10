package com.mtm.ksu.mobile.activity;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mtm.ksu.mobile.activity.SetorTabunganActivity.NoRekeningAfterFocusListener;/*FOKUS DISINI*/
import com.mtm.ksu.mobile.activitytask.ArrayAdapterAngsuranItem;
import com.mtm.ksu.mobile.activitytask.ArrayAdapterItem;
import com.mtm.ksu.mobile.activitytask.RekeningListTask;
import com.mtm.ksu.mobile.activitytask.RekeningListTaskListener;
import com.mtm.ksu.mobile.activitytask.TransactionTask;
import com.mtm.ksu.mobile.activitytask.TransactionTaskListener;
import com.mtm.ksu.mobile.common.ActionConstants;
import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.common.JsonResponseUtility;
import com.mtm.ksu.mobile.common.PrintingMessageFactory;
import com.mtm.ksu.mobile.model.Rekening;
import com.mtm.ksu.mobile.model.Transaction;
import com.mtm.ksu.mobile.request.ParameterRequest;
import com.mtm.ksu.mobile.request.TransactionRequest;
import com.mtm.ksu.mobile.service.CustomHttpClient;


public class SetorAngsuranActivity extends Activity implements
		OnClickListener, OnItemClickListener, RekeningListTaskListener,TransactionTaskListener {
	final Context context = this;
	final SetorAngsuranActivity transactionTaskListener = this;
	SharedPreferencesManager SharedPrefMgr;
	EditText noRekeningText, noRekeningDCAText, jumlahPokokText, jumlahBungaText, jumlahDendaText, jumlahAdminText,angsKeText,angsSisaText, totalAngsText, namaNasabahText, kriteriaText,tglJtText,searchNamaNasabahText;
	TextView noRekeningDCALabel;
	Button btnSimpanSetorAngsuran, btnCancelSetorAngsuran,  btnInternalSeach;
	ImageButton btnCariDataRekening;
	private DialogInterface.OnClickListener setorAngsuranListener,
			cancelSetorAngsuranListener;
	private AlertDialog konfirmasiDialog;
	AlertDialog alertDialogStores;
	String noRekening;
	String namaNasabah;
	boolean SINGLE_MODE = false;
	ProgressDialog progressDialog ;
	String generatedReferenceId;
	
	ListView listView;
		public void setGeneratedReferenceId(String generatedReferenceId) {
		this.generatedReferenceId = generatedReferenceId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.angsuran_form);
		listView = new ListView(this);
        
        View headerView = View.inflate(this, R.layout.search_header, null);
        
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(this);
        
		SharedPrefMgr = new SharedPreferencesManager(this);

		noRekeningText = (EditText) findViewById(R.id.trans_no_rekening);
		jumlahPokokText = (EditText) findViewById(R.id.trans_pokok);
		jumlahBungaText = (EditText) findViewById(R.id.trans_bunga);
		jumlahDendaText = (EditText) findViewById(R.id.trans_denda);
		namaNasabahText = (EditText) findViewById(R.id.trans_nama_nasabah);
        jumlahAdminText = (EditText) findViewById(R.id.trans_admin);
        totalAngsText   = (EditText) findViewById(R.id.total_angs);
        angsKeText   = (EditText) findViewById(R.id.angs_ke);
        angsSisaText = (EditText) findViewById(R.id.angs_sisa);
        tglJtText = (EditText) findViewById(R.id.tgl_jt);
		//searchNamaNasabahText = (EditText) findViewById(R.id.search_nama_nasabah);
		//((TextView)findViewById(R.id.trans_saldo_lbl)).setVisibility(EditText.GONE);
		//((TextView)findViewById(R.id.trans_saldo)).setVisibility(EditText.GONE);

		btnSimpanSetorAngsuran = (Button) findViewById(R.id.btn_setor_angsuran_simpan);
		btnCancelSetorAngsuran = (Button) findViewById(R.id.btn_setor_angsuran_cancel);
		btnCariDataRekening = (ImageButton)findViewById(R.id.btn_rekening_cari);
		btnInternalSeach = (Button)headerView.findViewById(R.id.btn_search_nama_nasabah);
		kriteriaText = (EditText)headerView.findViewById(R.id.search_nama_nasabah);

		btnSimpanSetorAngsuran.setOnClickListener(this);
		btnCancelSetorAngsuran.setOnClickListener(this);
		btnCariDataRekening.setOnClickListener(this);
		btnInternalSeach.setOnClickListener(this);
		
		noRekeningText.setOnFocusChangeListener(new NoRekeningAfterFocusListener());
        jumlahPokokText.setOnFocusChangeListener(new jumlahPokokAfterFocusListener());
        jumlahBungaText.setOnFocusChangeListener(new jumlahBungaAfterFocusListener());
        jumlahDendaText.setOnFocusChangeListener(new jumlahDendaAfterFocusListener());
        jumlahAdminText.setOnFocusChangeListener(new jumlahAdminAfterFocusListener());
		//Bundle extras = getIntent().getExtras();
		
	}
	
	@Override
	public void onBackPressed() {
		//do nothing. prevent back button pressed;
	}
	
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
			   finish();
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
	public void onClick(View view) {
		if(view == btnCariDataRekening ){
			showPopUpSearch();
			
		}else if(view == btnSimpanSetorAngsuran){

            NumberFormat nf = NumberFormat.getInstance();
            String jumlahBunga = jumlahBungaText.getText().toString();
            jumlahBunga = jumlahBunga.replace(",", "");
            int jumlahBungaFmt = Integer
                    .parseInt(jumlahBunga);

            String jumlahDenda = jumlahDendaText.getText().toString();
            jumlahDenda = jumlahDenda.replace(",", "");
            int jumlahDendaFmt = Integer
                    .parseInt(jumlahDenda);

            String jumlahAdmin = jumlahAdminText.getText().toString();
            jumlahAdmin = jumlahAdmin.replace(",", "");
            int jumlahAdminFmt = Integer
                    .parseInt(jumlahAdmin);

            String jumlahPokok = jumlahPokokText.getText().toString();
            jumlahPokok = jumlahPokok.replace(",", "");
            int jumlahPokokFmt = Integer
                    .parseInt(jumlahPokok);
            int total = jumlahPokokFmt + jumlahBungaFmt + jumlahDendaFmt + jumlahAdminFmt;
            String totalFmt = nf.format(total);

            totalAngsText.setText(totalFmt);

			showKonfirmasiTransaksi();
			
		}else if (view == btnCancelSetorAngsuran){
			 //kembali ke main menu
			 	Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
			    startActivity(mainMenuIntent);
		}else if (view == btnInternalSeach){
            /***
             * START
             * Isi nama nasabah kredit
             * Klik button cari
             ***/
			progressDialog = ProgressDialog.show(this, "", "Loading...");
			
			String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_KREDIT_SERVICE_FETCH_DATA_LIST;
			RekeningListTask task = new RekeningListTask(this);
			ParameterRequest request = null;
			if(kriteriaText.getText().toString() != null && kriteriaText.getText().toString() !=""){
				request = new ParameterRequest();
				request.setKriteria(kriteriaText.getText().toString());
			}
			task.setRequest(request);
			task.execute(URLFetchDataRekeningListAction);
            /***
             * END
             * Isi nama nasabah kredit
             * Klik button cari
             ***/
		}
		
	}
	
	private boolean validate() {
		boolean result = false;

		if (!"".equals(noRekeningText.getText().toString())
				&& !"".equals(jumlahPokokText.getText().toString())
                && !"".equals(jumlahBungaText.getText().toString())
                && !"".equals(jumlahAdminText.getText().toString())
                && !"".equals(jumlahDendaText.getText().toString())
                ) {
			result = true;

		}

		return result;
	}

	private void showKonfirmasiTransaksi() {
		
		if (!validate()) {
			String message = "Mohon isi kolom yang masih kosong !";
			new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(message)
		    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).show();	
		} else {
				if (!"".equals(jumlahPokokText.getText().toString())) {
					NumberFormat nf = NumberFormat.getInstance();
					String jumlahPokok = jumlahPokokText.getText()
							.toString();
					jumlahPokok = jumlahPokok.replace(",", "");
					String jumlahPokokFmt = nf.format(Integer
							.parseInt(jumlahPokok));
					
					String jumlahBunga = jumlahBungaText.getText()
					.toString();
					jumlahBunga = jumlahBunga.replace(",", "");
					String jumlahBungaFmt = nf.format(Integer
							.parseInt(jumlahBunga));
					
					String jumlahDenda = jumlahDendaText.getText()
					.toString();
					jumlahDenda = jumlahDenda.replace(",", "");
					String jumlahDendaFmt = nf.format(Integer
							.parseInt(jumlahDenda));

                    String jumlahAdmin = jumlahAdminText.getText()
                            .toString();
                    jumlahAdmin = jumlahAdmin.replace(",", "");
                    String jumlahAdminFmt = nf.format(Integer
                            .parseInt(jumlahAdmin));

                    String angsKe = angsKeText.getText()
                            .toString();
                    angsKe = angsKe.replace(",", "");
                    String angsKeFmt = nf.format(Integer
                            .parseInt(angsKe));

                    String angsSisa = angsSisaText.getText()
                            .toString();
                    String tglJt = tglJtText.getText()
                            .toString();
                    /*angsSisa = angsSisa.replace(",", "");
                    String angsSisaFmt = nf.format(Integer
                            .parseInt(angsSisa));*/

                    String totalAngs = totalAngsText.getText()
                            .toString();
                    totalAngs = totalAngs.replace(",", "");
                    String totalAngsFmt = nf.format(Integer
                            .parseInt(totalAngs));
					
					String konfirmasiStr = "No. Rekening : " + noRekeningText.getText().toString()
							+ "\n\n" + "Nasabah   : " + namaNasabahText.getText().toString()
							+ "\n\n" + "Jml Pokok : Rp. " + jumlahPokokFmt
							+ "\n\n" + "Jml Margin : Rp. " + jumlahBungaFmt
							+ "\n\n" + "Jml Denda : Rp. " + jumlahDendaFmt
                            + "\n\n" + "Jml Admin : Rp. " + jumlahAdminFmt
                            + "\n\n" + "Angs ke   : " + angsKe
                            + "\n\n" + "Sisa angs   : " + angsSisa
                            + "\n\n" + "Tgl JT   : " + tglJt
                            + "\n\n" + "Total Angs : Rp. " + totalAngsFmt
							+ "\n\n";

					LayoutInflater factory = LayoutInflater
							.from(SetorAngsuranActivity.this);
					konfirmasiDialog = new AlertDialog.Builder(
							SetorAngsuranActivity.this).create();

					final View textEntryView = factory.inflate(
							R.layout.konfirmasi_transaksi_item, null);
					final TextView konfirmasiLbl = (TextView) textEntryView
							.findViewById(R.id.str_konfirmasi);
					konfirmasiLbl.setText(konfirmasiStr);

                    /***
                     * START
                     * Muncul konfirmasi setoran angsuran
                     * Klik button simpan
                     ***/
					setorAngsuranListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							progressDialog = ProgressDialog.show(context, "",
									"Processing...");
							String URLSetorAngsuranAction = Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_SETOR_ANGSURAN;
							
							TransactionTask task = new TransactionTask(
									transactionTaskListener);
							
							TransactionRequest request = new TransactionRequest();
							request.setNoRekening(noRekeningText.getText().toString());
							request.setNamaNasabah(namaNasabahText.getText().toString());
							request.setJumlahPokok(jumlahPokokText.getText().toString());
							request.setJumlahBunga(jumlahBungaText.getText().toString());
							request.setJumlahDenda(jumlahDendaText.getText().toString());
                            request.setJumlahAdmin(jumlahAdminText.getText().toString());
                            request.setAngsKe(angsKeText.getText().toString());
                            request.setAngsSisa(angsSisaText.getText().toString());
                            request.setAngsSisa(tglJtText.getText().toString());
                            request.setTotalAngs(totalAngsText.getText().toString());
							request.setTanggalTrans(SharedPrefMgr.getTanggalOperasional());
							request.setUserId(SharedPrefMgr.getUserId());
							request.setKodeTrans(SharedPrefMgr.getKodeTransAngsuran());
                            request.setImei(SharedPrefMgr.getImei());
							
							task.setRequest(request);
							task.execute(URLSetorAngsuranAction);
							dialog.dismiss();

						}

					};

					cancelSetorAngsuranListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (dialog != null)
								dialog.dismiss();

						}

					};

					konfirmasiDialog.setView(textEntryView);
					konfirmasiDialog.setTitle("Konfirmasi Setor Angsuran");
					konfirmasiDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							getResources().getString(R.string.tombol_simpan),
							setorAngsuranListener);
					konfirmasiDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							getResources().getString(R.string.tombol_batal),
							cancelSetorAngsuranListener);
					konfirmasiDialog.show();
                    /***
                     * END
                     * Muncul konfirmasi setoran angsuran
                     * Klik button simpan
                     ***/
				}

		}//end if else validate()

		
	}

	private void showPopUpSearch() {
		//P BUDIMAN
		//String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_FETCH_DATA_LIST;
		//ANG
        /***
         * START
         * Isi nama nasabah kredit
         * Klik button cari
         ***/
		String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_KREDIT_SERVICE_FETCH_DATA_LIST;
		RekeningListTask task = new RekeningListTask(this);
		task.execute(URLFetchDataRekeningListAction);
        /***
         * END
         * Isi nama nasabah kredit
         * Klik button cari
         ***/
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Context context = view.getContext();
		LinearLayout linearLayoutParent = (LinearLayout) view;
		TextView itemRekeningTxt = (TextView) linearLayoutParent.getChildAt(0);
		TextView itemNasabahTxt = (TextView) linearLayoutParent.getChildAt(1);
		
		noRekeningText.setText(itemRekeningTxt.getText().toString());
		namaNasabahText.setText(itemNasabahTxt.getText().toString().substring(3));
	   ((SetorAngsuranActivity) context).alertDialogStores.cancel();
	}

	@Override
	public void onFetchComplete(List<Rekening> data) {
		if(progressDialog != null)  progressDialog.dismiss();
		ArrayAdapterAngsuranItem adapter = new ArrayAdapterAngsuranItem(this, R.layout.list_view_row_item, data);
	      
		if(alertDialogStores == null){
			Rekening[] rekeningArray = new Rekening[data.size()];
			data.toArray(rekeningArray);
			// our adapter instance
	        
	        listView.setAdapter(adapter);
	         
	        // put the ListView in the pop up
	        alertDialogStores = new AlertDialog.Builder(SetorAngsuranActivity.this)
		        .setView(listView)
		        .setTitle("Cari (nama)")
		        .show();
	        alertDialogStores.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	       // noRekeningText.setOnFocusChangeListener(new NoRekeningAfterFocusListener());
	        //search_nama_nasabah
		}else{
			listView.setAdapter(adapter);
			listView.refreshDrawableState();
			alertDialogStores.show();
			//alertDialogStores.
			
		}
		
	}

	@Override
	public void onFetchFailure(String msg) {
		new AlertDialog.Builder(context).setTitle("MTech Mobile")
		.setMessage(msg)
	    .setNeutralButton("TUTUP", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
		if (progressDialog != null)progressDialog.dismiss();
	
	}
	
	
	@Override
	public void onTransactionComplete(Transaction data) {
		if (progressDialog != null)	progressDialog.dismiss();
		
		TransactionRequest request = new TransactionRequest();
		
		request.setNoRekening(noRekeningText.getText().toString());
		NumberFormat nf = NumberFormat.getInstance();
		
		String jumlahPokok = jumlahPokokText.getText().toString();
		jumlahPokok = jumlahPokok.replace(",", "");
		String jumlahPokokFmt = nf.format(Integer
				.parseInt(jumlahPokok));
		
		String jumlahBunga = jumlahBungaText.getText().toString();
		jumlahBunga = jumlahBunga.replace(",", "");
		String jumlahBungaFmt = nf.format(Integer
				.parseInt(jumlahBunga));
		
		String jumlahDenda = jumlahDendaText.getText().toString();
		jumlahDenda = jumlahDenda.replace(",", "");
		String jumlahDendaFmt = nf.format(Integer
				.parseInt(jumlahDenda));

        String jumlahAdmin = jumlahAdminText.getText().toString();
        jumlahAdmin = jumlahAdmin.replace(",", "");
        String jumlahAdminFmt = nf.format(Integer
                .parseInt(jumlahAdmin));

        String angsKe = angsKeText.getText().toString();
        angsKe = angsKe.replace(",", "");
        String angsKeFmt = nf.format(Integer
                .parseInt(angsKe));

        String angsSisa = angsSisaText.getText().toString();
        String tglJt = tglJtText.getText().toString();
        /*angsKe = angsKe.replace(",", "");
        String angsKeFmt = nf.format(Integer
                .parseInt(angsKe));*/

        String totalAngs = totalAngsText.getText().toString();
        totalAngs = totalAngs.replace(",", "");
        String totalAngsFmt = nf.format(Integer
                .parseInt(totalAngs));
		
		
		request.setJumlahPokok(jumlahPokokFmt);
		request.setJumlahBunga(jumlahBungaFmt);
		request.setJumlahDenda(jumlahDendaFmt);
        request.setJumlahAdmin(jumlahAdminFmt);
        request.setAngsKe(angsKeFmt);
        request.setAngsSisa(angsSisa);
        request.setTglJt(tglJt);
        request.setTotalAngs(totalAngsFmt);
		request.setTanggalTrans(SharedPrefMgr.getTanggalOperasional());
		request.setTipeTrans(Constants.PRINTING_TRANS_TYPE_ANGSURAN);
		request.setNamaPerusahaan(SharedPrefMgr.getCompanyName());
		request.setNamaNasabah(namaNasabahText.getText().toString());
		//no kuitansi
		request.setNoKuitansi(data.getNoKuitansi());
        /*
        * Khusus kasa arta
        * */
        int jumlahBungaInt = Integer
                .parseInt(jumlahBunga);
        int jumlahAdminInt = Integer
                .parseInt(jumlahAdmin);
        int total = jumlahBungaInt + jumlahAdminInt;
        String totalBungaAdminFmt = nf.format(total);
        request.setTotalBungaAdmin(totalBungaAdminFmt);

        /*
        * end khusus kasaarta*/

         new AlertDialog.Builder(context).setTitle("MTech Mobile")
		.setMessage(data.getServerResponseMessage())
	    .setNeutralButton("TUTUP", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
		
		String receipt = PrintingMessageFactory.ReceiptMessage(request);
		Intent intent = new Intent(context, PrintReceiptActivity.class);
		intent.putExtra("Receipt", receipt);

		startActivity(intent);
		
		//if (progressDialog != null)progressDialog.dismiss();
		finish();
	 
		
		
	}

	@Override
	public void onTransactionFailure(String msg) {
		new AlertDialog.Builder(context).setTitle("Transaction Response")
		.setMessage(msg)
	    .setNeutralButton("TUTUP", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
		if (progressDialog != null)progressDialog.dismiss();
		
	}
	
	
	/*Menangani event after focus NoRekeningText, show Nama Nasabah di NamaNasabahText*/
	class NoRekeningAfterFocusListener implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(!hasFocus){
				String URLFetchDataRekeningInfoAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_KREDIT_SERVICE_FETCH_DATA_INFO;
				DoGetRekeningInfoTask task = new DoGetRekeningInfoTask();
				task.execute(URLFetchDataRekeningInfoAction);
			}
			
		}
		
		
	}

    class jumlahPokokAfterFocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                NumberFormat nf = NumberFormat.getInstance();
                String jumlahBunga = jumlahBungaText.getText().toString();
                jumlahBunga = jumlahBunga.replace(",", "");
                int jumlahBungaFmt = Integer
                        .parseInt(jumlahBunga);

                String jumlahDenda = jumlahDendaText.getText().toString();
                jumlahDenda = jumlahDenda.replace(",", "");
                int jumlahDendaFmt = Integer
                        .parseInt(jumlahDenda);

                String jumlahAdmin = jumlahAdminText.getText().toString();
                jumlahAdmin = jumlahAdmin.replace(",", "");
                int jumlahAdminFmt = Integer
                        .parseInt(jumlahAdmin);

                String jumlahPokok = jumlahPokokText.getText().toString();
                jumlahPokok = jumlahPokok.replace(",", "");
                int jumlahPokokFmt = Integer
                        .parseInt(jumlahPokok);
                int total = jumlahPokokFmt + jumlahBungaFmt + jumlahDendaFmt + jumlahAdminFmt;
                String totalFmt = nf.format(total);

                totalAngsText.setText(totalFmt);

            }
        }
    }
    class jumlahBungaAfterFocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                NumberFormat nf = NumberFormat.getInstance();

                String jumlahBunga = jumlahBungaText.getText().toString();
                jumlahBunga = jumlahBunga.replace(",", "");
                int jumlahBungaFmt = Integer
                        .parseInt(jumlahBunga);

                String jumlahDenda = jumlahDendaText.getText().toString();
                jumlahDenda = jumlahDenda.replace(",", "");
                int jumlahDendaFmt = Integer
                        .parseInt(jumlahDenda);

                String jumlahAdmin = jumlahAdminText.getText().toString();
                jumlahAdmin = jumlahAdmin.replace(",", "");
                int jumlahAdminFmt = Integer
                        .parseInt(jumlahAdmin);

                String jumlahPokok = jumlahPokokText.getText().toString();
                jumlahPokok = jumlahPokok.replace(",", "");
                int jumlahPokokFmt = Integer
                        .parseInt(jumlahPokok);
                int total = jumlahPokokFmt + jumlahBungaFmt + jumlahDendaFmt + jumlahAdminFmt;
                String totalFmt = nf.format(total);

                totalAngsText.setText(totalFmt);

            }
        }
    }
    class jumlahDendaAfterFocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                NumberFormat nf = NumberFormat.getInstance();

                String jumlahBunga = jumlahBungaText.getText().toString();
                jumlahBunga = jumlahBunga.replace(",", "");
                int jumlahBungaFmt = Integer
                        .parseInt(jumlahBunga);

                String jumlahDenda = jumlahDendaText.getText().toString();
                jumlahDenda = jumlahDenda.replace(",", "");
                int jumlahDendaFmt = Integer
                        .parseInt(jumlahDenda);

                String jumlahAdmin = jumlahAdminText.getText().toString();
                jumlahAdmin = jumlahAdmin.replace(",", "");
                int jumlahAdminFmt = Integer
                        .parseInt(jumlahAdmin);

                String jumlahPokok = jumlahPokokText.getText().toString();
                jumlahPokok = jumlahPokok.replace(",", "");
                int jumlahPokokFmt = Integer
                        .parseInt(jumlahPokok);
                int total = jumlahPokokFmt + jumlahBungaFmt + jumlahDendaFmt + jumlahAdminFmt;
                String totalFmt = nf.format(total);

                totalAngsText.setText(totalFmt);

            }
        }
    }
    class jumlahAdminAfterFocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                NumberFormat nf = NumberFormat.getInstance();

                String jumlahBunga = jumlahBungaText.getText().toString();
                jumlahBunga = jumlahBunga.replace(",", "");
                int jumlahBungaFmt = Integer
                        .parseInt(jumlahBunga);

                String jumlahDenda = jumlahDendaText.getText().toString();
                jumlahDenda = jumlahDenda.replace(",", "");
                int jumlahDendaFmt = Integer
                        .parseInt(jumlahDenda);

                String jumlahAdmin = jumlahAdminText.getText().toString();
                jumlahAdmin = jumlahAdmin.replace(",", "");
                int jumlahAdminFmt = Integer
                        .parseInt(jumlahAdmin);

                String jumlahPokok = jumlahPokokText.getText().toString();
                jumlahPokok = jumlahPokok.replace(",", "");
                int jumlahPokokFmt = Integer
                        .parseInt(jumlahPokok);
                int total = jumlahPokokFmt + jumlahBungaFmt + jumlahDendaFmt + jumlahAdminFmt;
                String totalFmt = nf.format(total);

                totalAngsText.setText(totalFmt);

            }
        }
    }
	
	//class untuk handling proses change focus no rekening
	private class DoGetRekeningInfoTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String output = null;
			if(params == null) return null;
			String url = params[0];
			 try {
				 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				 postParameters.add(new BasicNameValuePair("NoRekening", noRekeningText.getText().toString()));
					
	 			
				 output = CustomHttpClient.executeHttpPost(url, postParameters);
				 
			 }catch(IOException e){
				 
			 } catch (Exception e) {
				//e.printStackTrace();
			 }
			
			return output;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				JsonResponseUtility _JsonResponse = new JsonResponseUtility(result);
				if(Constants.SERVER_SUCCESS_RESPONSE.equals(_JsonResponse.getResponseStatus())){
					if(_JsonResponse.getResponseDetail() != null){
						try {
							String namaNasabah = _JsonResponse.getResponseDetail().getString("NAMA_NASABAH").trim();
                            String tglJT = _JsonResponse.getResponseDetail().getString("tglJT").trim();

							String jPokok = _JsonResponse.getResponseDetail().getString("JPokok").trim();

							NumberFormat nf = NumberFormat.getInstance();
							String jumlahPokokFmt = nf.format(Double
									.parseDouble(jPokok));
							
							String jBunga = _JsonResponse.getResponseDetail().getString("JBunga").trim();
							NumberFormat nf1 = NumberFormat.getInstance();
							
							String jumlahBungaFmt = nf1.format(Double
									.parseDouble(jBunga));
							
							String jDenda = _JsonResponse.getResponseDetail().getString("JDenda").trim();
							NumberFormat nf2 = NumberFormat.getInstance();
							
							String jumlahDendaFmt = nf2.format(Double
									.parseDouble(jDenda));

                            String jAdmin = _JsonResponse.getResponseDetail().getString("JAdmin").trim();
                            NumberFormat nf3 = NumberFormat.getInstance();

                            String jumlahAdminFmt = nf3.format(Double
                                    .parseDouble(jAdmin));

                            String angsKe = _JsonResponse.getResponseDetail().getString("angsKe").trim();
                            NumberFormat nf5 = NumberFormat.getInstance();

                            String angsKeFmt = nf5.format(Double
                                    .parseDouble(angsKe));

                            String angsSisa = _JsonResponse.getResponseDetail().getString("sisaAngs").trim();
                            NumberFormat nf6 = NumberFormat.getInstance();

                            String angsSisaFmt = nf6.format(Double
                                    .parseDouble(angsSisa));

                            String totalAngs = _JsonResponse.getResponseDetail().getString("totalAngs").trim();
                            NumberFormat nf4 = NumberFormat.getInstance();

                            String totalAngsFmt = nf4.format(Double
                                    .parseDouble(totalAngs));
							
							namaNasabahText.setText(namaNasabah);
							jumlahPokokText.setText(jumlahPokokFmt);
							jumlahBungaText.setText(jumlahBungaFmt);
							jumlahDendaText.setText(jumlahDendaFmt);
                            jumlahAdminText.setText(jumlahAdminFmt);
                            angsKeText.setText(angsKeFmt);
                            angsSisaText.setText(angsSisaFmt);
                            tglJtText.setText(tglJT);
                            totalAngsText.setText(totalAngsFmt);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}


	
	
	
}
