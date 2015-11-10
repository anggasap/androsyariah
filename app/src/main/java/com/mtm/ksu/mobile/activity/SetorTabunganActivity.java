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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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


public class SetorTabunganActivity extends Activity implements
		OnClickListener, OnItemClickListener, RekeningListTaskListener,TransactionTaskListener {
	final Context context = this;
	final SetorTabunganActivity transactionTaskListener = this;
	SharedPreferencesManager SharedPrefMgr;
	EditText noRekeningText, noRekeningDCAText, jumlahPembayaranText, namaNasabahText, kriteriaText, saldoText, saldoTabAftText;
	TextView noRekeningDCALabel;
	Button btnSimpanSetorTabungan, btnCancelSetorTabungan,  btnInternalSeach;
	ImageButton btnCariDataRekening;
	private DialogInterface.OnClickListener setorTabunganListener,
			cancelSetorTabunganListener;
	private AlertDialog konfirmasiDialog;
	Dialog alertDialogStores;
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
		setContentView(R.layout.transaksi_form);
		listView = new ListView(this);
        
        View headerView = View.inflate(this, R.layout.search_header, null);
        
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(this);
        
		SharedPrefMgr = new SharedPreferencesManager(this);

		noRekeningText = (EditText) findViewById(R.id.trans_no_rekening);
		jumlahPembayaranText = (EditText) findViewById(R.id.trans_jumlah);
		namaNasabahText = (EditText) findViewById(R.id.trans_nama_nasabah);
		saldoText = (EditText)findViewById(R.id.trans_saldo);
        saldoTabAftText = (EditText)findViewById(R.id.saldotab_aft);

		btnSimpanSetorTabungan = (Button) findViewById(R.id.btn_setor_tabungan_simpan);
		btnCancelSetorTabungan = (Button) findViewById(R.id.btn_setor_tabungan_cancel);
		btnCariDataRekening = (ImageButton)findViewById(R.id.btn_rekening_cari);
		btnInternalSeach = (Button)headerView.findViewById(R.id.btn_search_nama_nasabah);
		kriteriaText = (EditText)headerView.findViewById(R.id.search_nama_nasabah);

		btnSimpanSetorTabungan.setOnClickListener(this);
		btnCancelSetorTabungan.setOnClickListener(this);
		btnCariDataRekening.setOnClickListener(this);
		btnInternalSeach.setOnClickListener(this);
		
		noRekeningText.setOnFocusChangeListener(new NoRekeningAfterFocusListener());
        //saldoText.setOnFocusChangeListener(new saldoTabAfterFocusListener());
        jumlahPembayaranText.setOnFocusChangeListener(new jumlahPembayaranAfterFocusListener());
		/*
		btnInternalSeach.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            	btnInternalSeach.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(btnInternalSeach, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
		btnInternalSeach.requestFocus();
		*/
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
			
		}else if(view == btnSimpanSetorTabungan){
			showKonfirmasiTransaksi();
			
		}else if (view == btnCancelSetorTabungan){
			 //kembali ke main menu
			 	Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
			    startActivity(mainMenuIntent);
			    
		}else if (view == btnInternalSeach){
			progressDialog = ProgressDialog.show(this, "", "Loading...");
			
			String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_FETCH_DATA_LIST;
			RekeningListTask task = new RekeningListTask(this);
			ParameterRequest request = null;
			if(kriteriaText.getText().toString() != null && kriteriaText.getText().toString() !=""){
				request = new ParameterRequest();
				request.setKriteria(kriteriaText.getText().toString());
			}
			task.setRequest(request);
			task.execute(URLFetchDataRekeningListAction);
			
		}
		
	}
	
	private boolean validate() {
		boolean result = false;

		if (!"".equals(noRekeningText.getText().toString())
				&& !"".equals(jumlahPembayaranText.getText().toString())) {
			result = true;

		}

		return result;
	}

	private void showKonfirmasiTransaksi() {
		
		if (!validate()) {
			String message = "No. Rekening, Nama Nasabah dan Jumlah Pembayaran Tidak Boleh Kosong !";
			new AlertDialog.Builder(context).setTitle("MTech Mobile").setMessage(message)
		    .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).show();	
		} else {
				if (!"".equals(jumlahPembayaranText.getText().toString())) {
					NumberFormat nf = NumberFormat.getInstance();
					String jumlahPembayaran = jumlahPembayaranText.getText()
							.toString();
					String jumlahSetoranFmt = nf.format(Integer
							.parseInt(jumlahPembayaran));
                    //saldoTabAftText
                    final String saldoTabAft = saldoTabAftText.getText()
                            .toString();

                    /*HITUNG SALDO AKHIR SETELAH FOCUS OUT JML PEMBAYARAN (KLIK SIMPAN)*/
                    String jumlahSetoranV = jumlahPembayaranText.getText().toString();
                    jumlahSetoranV = jumlahSetoranV.replace(",", "");
                    int jumlahSetoranFmtV = Integer
                            .parseInt(jumlahSetoranV);

                    String saldoTabV = saldoText.getText().toString();
                    saldoTabV = saldoTabV.replace(",", "");
                    double saldoTabFmt = Double
                            .parseDouble(saldoTabV);

                    double saldoAkhir = jumlahSetoranFmtV + saldoTabFmt;
                    String saldoAkhirFmt = nf.format(saldoAkhir);
                    saldoTabAftText.setText(saldoAkhirFmt);

                    String saldoTabAftStr = saldoTabAftText.getText()
                            .toString();

					String konfirmasiStr = "No. Rekening : " + noRekeningText.getText().toString()
							+ "\n\n" + "Nasabah : " + namaNasabahText.getText().toString()
							+ "\n\n" + "Jml Setoran : Rp. " + jumlahSetoranFmt
                            + "\n\n" + "Saldo Akhir : Rp. " + saldoTabAftStr
							+ "\n\n";

					LayoutInflater factory = LayoutInflater
							.from(SetorTabunganActivity.this);
					konfirmasiDialog = new AlertDialog.Builder(
							SetorTabunganActivity.this).create();

					final View textEntryView = factory.inflate(
							R.layout.konfirmasi_transaksi_item, null);
					final TextView konfirmasiLbl = (TextView) textEntryView
							.findViewById(R.id.str_konfirmasi);
					konfirmasiLbl.setText(konfirmasiStr);

					setorTabunganListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							progressDialog = ProgressDialog.show(context, "",
									"Processing...");
							String URLSetorTabunganAction = Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_SETOR_TABUNGAN;
							
							TransactionTask task = new TransactionTask(
									transactionTaskListener);
							
							TransactionRequest request = new TransactionRequest();
							request.setNoRekening(noRekeningText.getText().toString());
							request.setNamaNasabah(namaNasabahText.getText().toString());
							request.setJumlahTrans(jumlahPembayaranText.getText().toString());
							request.setTanggalTrans(SharedPrefMgr.getTanggalOperasional());
							request.setUserId(SharedPrefMgr.getUserId());
							request.setKodeTrans(SharedPrefMgr.getKodeTransSetor());
							request.setImei(SharedPrefMgr.getImei());

                            request.setSaldoTabAft(saldoTabAftText.getText().toString());
							
							
							task.setRequest(request);
							task.execute(URLSetorTabunganAction);
							dialog.dismiss();

						}

					};

					cancelSetorTabunganListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (dialog != null)
								dialog.dismiss();

						}

					};

					konfirmasiDialog.setView(textEntryView);
					konfirmasiDialog.setTitle("Konfirmasi Setor Tabungan");
					konfirmasiDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							getResources().getString(R.string.tombol_simpan),
							setorTabunganListener);
					konfirmasiDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							getResources().getString(R.string.tombol_batal),
							cancelSetorTabunganListener);
					konfirmasiDialog.show();
					
				}

		}//end if else validate()

		
	}

	private void showPopUpSearch() {
		btnInternalSeach.clearFocus();
		listView.clearFocus();
		String URLFetchDataRekeningListAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_FETCH_DATA_LIST;
		RekeningListTask task = new RekeningListTask(this);
		task.execute(URLFetchDataRekeningListAction);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Context context = view.getContext();
		LinearLayout linearLayoutParent = (LinearLayout) view;
		TextView itemRekeningTxt = (TextView) linearLayoutParent.getChildAt(0);
		TextView itemNasabahTxt = (TextView) linearLayoutParent.getChildAt(1);
		TextView itemSaldoTxt = (TextView)linearLayoutParent.getChildAt(2);
		
		noRekeningText.setText(itemRekeningTxt.getText().toString());
		namaNasabahText.setText(itemNasabahTxt.getText().toString().substring(3));
		saldoText.setText(itemSaldoTxt.getText().toString());
        saldoTabAftText.setText(itemSaldoTxt.getText().toString());
		
	   ((SetorTabunganActivity) context).alertDialogStores.cancel();
	}

	@Override
	public void onFetchComplete(List<Rekening> data) {
		if(progressDialog != null)  progressDialog.dismiss();
		ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item, data);
	      
		if(alertDialogStores == null){
			Rekening[] rekeningArray = new Rekening[data.size()];
			data.toArray(rekeningArray);
			// our adapter instance
	        
	        listView.setAdapter(adapter);
	         
	        // put the ListView in the pop up
	        alertDialogStores = new AlertDialog.Builder(SetorTabunganActivity.this)
		        .setView(listView)
		        .setTitle("Cari ( nama )")
		        .show();
	        alertDialogStores.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		}else{
			listView.setAdapter(adapter);
			listView.refreshDrawableState();
			alertDialogStores.show();
			//alertDialogStores.
			
		}
		if (progressDialog != null)progressDialog.dismiss();
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
		String jumlahPembayaran = jumlahPembayaranText.getText().toString();
		String jumlahPembayaranFmt = nf.format(Integer
				.parseInt(jumlahPembayaran));
		request.setJumlahTrans(jumlahPembayaranFmt);
		request.setTanggalTrans(SharedPrefMgr.getTanggalOperasional());
		request.setTipeTrans(Constants.PRINTING_TRANS_TYPE_SETOR_TABUNGAN);
		request.setNamaPerusahaan(SharedPrefMgr.getCompanyName());
		request.setNamaNasabah(namaNasabahText.getText().toString());
		//no kuitansi
		request.setNoKuitansi(data.getNoKuitansi());
        //saldo awal tabungan
        String saldoTabAwal = saldoText.getText().toString();
        request.setSaldoTabAwal(saldoTabAwal);
        //saldo tabungan
        String saldoTabAft = saldoTabAftText.getText().toString();
        request.setSaldoTabAft(saldoTabAft);
		
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
				String URLFetchDataRekeningInfoAction= Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_FETCH_DATA_INFO;
				DoGetRekeningInfoTask task = new DoGetRekeningInfoTask();
				task.execute(URLFetchDataRekeningInfoAction);
			}
		}
	}

    class jumlahPembayaranAfterFocusListener implements OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                if("".equals(jumlahPembayaranText.getText().toString())){
                    jumlahPembayaranText.setText("0");

                    NumberFormat nf = NumberFormat.getInstance();
                    String jumlahSetoran = jumlahPembayaranText.getText().toString();
                    jumlahSetoran = jumlahSetoran.replace(",", "");
                    int jumlahSetoranFmt = Integer
                            .parseInt(jumlahSetoran);
                    String saldoTab = saldoText.getText().toString();
                    saldoTab = saldoTab.replace(",", "");
                    double saldoTabFmt = Double
                            .parseDouble(saldoTab);

                    double saldoAkhir = jumlahSetoranFmt + saldoTabFmt;
                    String saldoAkhirFmt = nf.format(saldoAkhir);
                    saldoTabAftText.setText(saldoAkhirFmt);
                }else{
                    NumberFormat nf = NumberFormat.getInstance();
                    String jumlahSetoran = jumlahPembayaranText.getText().toString();
                    jumlahSetoran = jumlahSetoran.replace(",", "");
                    int jumlahSetoranFmt = Integer
                            .parseInt(jumlahSetoran);

                    String saldoTab = saldoText.getText().toString();
                    saldoTab = saldoTab.replace(",", "");
                    double saldoTabFmt = Double
                            .parseDouble(saldoTab);

                    double saldoAkhir = jumlahSetoranFmt + saldoTabFmt;
                    String saldoAkhirFmt = nf.format(saldoAkhir);
                    saldoTabAftText.setText(saldoAkhirFmt);
                }


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
							String saldo = _JsonResponse.getResponseDetail().getString("SALDO_AKHIR").trim();
							NumberFormat nf = NumberFormat.getInstance();
							
							String jumlahSaldoFmt = nf.format(Double
									.parseDouble(saldo));
							namaNasabahText.setText(namaNasabah);
							saldoText.setText(jumlahSaldoFmt);
                            saldoTabAftText.setText(jumlahSaldoFmt);
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
