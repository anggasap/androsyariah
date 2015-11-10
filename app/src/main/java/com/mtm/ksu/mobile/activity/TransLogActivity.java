package com.mtm.ksu.mobile.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mtm.ksu.mobile.activitytask.TransLogAdapter;
import com.mtm.ksu.mobile.activitytask.TransLogListListener;
import com.mtm.ksu.mobile.activitytask.TransLogListTask;
import com.mtm.ksu.mobile.common.ActionConstants;
import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.model.Transaction;
import com.mtm.ksu.mobile.request.TransactionRequest;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class TransLogActivity extends ListActivity implements TransLogListListener {
	private ProgressDialog dialog;
	SharedPreferencesManager SharedPrefMgr;
	private AlertDialog infoDialog;
	
	protected static final int CONTEXTMENU_OPTION_INFO = 1; 
	//protected static final int CONTEXTMENU_OPTION_REVERSAL_TRANSACTION = 2; 
	protected static final int CONTEXTMENU_OPTION_PRINT = 2; 
	
	private DialogInterface.OnClickListener closeListener;
	ListView listView;
	TransLogAdapter adapter;
	
	Map<String, Transaction> transactionMap = new HashMap<String, Transaction>();
	List<Transaction> transactionList ;
	
	//---------------bluetooth
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;
	
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	
	
	//----------------------------------------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPrefMgr = new SharedPreferencesManager(this);
		
		
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
			 //menu.add(Menu.NONE, CONTEXTMENU_OPTION_REVERSAL_TRANSACTION, 1, "Void"); 
			 menu.add(Menu.NONE, CONTEXTMENU_OPTION_PRINT, 0, "Print"); 
			 
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
			//menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "Print Total Transaksi");
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
				try {
					closeBT();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			TextView transIdText = (TextView) linearLayoutParent.getChildAt(3);
			Transaction transaction = transactionMap.get(transIdText.getText().toString());
		
			switch (item.getItemId()) {
			 	case CONTEXTMENU_OPTION_INFO:
			 		
			 		LayoutInflater factory = LayoutInflater.from(TransLogActivity.this);
			 		infoDialog = new AlertDialog.Builder(TransLogActivity.this).create();
			 		final View infoView = factory.inflate(R.layout.translog_info, null);
			 		final TextView noRekeningText, tipeTransaksiText, namaNasabahText, nilaiTransaksiText;
			 		noRekeningText = (TextView)infoView.findViewById(R.id.transloginfo_no_rekening);
			        tipeTransaksiText = (TextView)infoView.findViewById(R.id.transloginfo_type);
			        namaNasabahText = (TextView)infoView.findViewById(R.id.transloginfo_nama_nasabah);
			        nilaiTransaksiText = (TextView)infoView.findViewById(R.id.transloginfo_nominal_transaksi);
			        
			        
			        final Button closeBtn = (Button) infoView.findViewById(R.id.info_trx_btn_close);
			        closeBtn.setVisibility(View.GONE);
			      
					NumberFormat nf = NumberFormat.getInstance();
					
			      
					noRekeningText.setText        ("No Rekening      : \n \t\t\t" + transaction.getNoRekening());
					tipeTransaksiText.setText     ("Tipe Trans.  : \n \t\t\t"+ transaction.getTransType());
					namaNasabahText.setText       ("Nama Nasabah     : \n \t\t\t"+ transaction.getNamaNasabah());
					nilaiTransaksiText.setText    ("Nilai Transaksi  : \n \t\t\t"+ nf.format(Double.parseDouble(transaction.getJumlahTrans())));
					
					closeListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					};
					
					infoDialog.setView(infoView);
					infoDialog.setTitle("Informasi Transaksi");
					infoDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Close",
							closeListener);
					
					infoDialog.show();
			 		break;
			 	
			 	
			 		
			 	case CONTEXTMENU_OPTION_PRINT:
			 		/*
			 		ParameterModel model = new ParameterModel();
			 		model.setNamaOutlet(SharedPrefMgr.getOutletName());
			 		model.setNoRekening(transaction.getNoRekeningKredit());
			 		model.setJumlahPembayaran(transaction.getNilaiTrans());
			 		model.setWaktuTransaksi(transaction.getWaktuTransaksi());
			 		model.setNamaNasabah(transaction.getNamaNasabah());
			 		model.setNoReferensi(transaction.getNoReferensi());
			 		model.setUserName(transaction.getUserName());
			 		
			 		
			 		try {
						findBT();
						openBT();
						sendData(PrintingMessageFactory.ReceiptMessage(model));
						//closeBT();
					} catch (IOException ex) {
					}*/
			 		
			 		break;
			}
		}
		return true;
	}


	private void initView() {
		dialog = ProgressDialog.show(this, "", "Loading...");
		String URLTransLogListAction = Constants.HTTP_SERVER_SERVICE + ActionConstants.REKENING_SERVICE_TRANSLOG_LIST;
		TransLogListTask task = new TransLogListTask(this);
		TransactionRequest request = new TransactionRequest();
		request.setTanggalTrans(SharedPrefMgr.getTanggalOperasional());
		request.setUserId(SharedPrefMgr.getUserId());
		request.setImei(SharedPrefMgr.getImei());
		task.setRequest(request);
	    task.execute(URLTransLogListAction);
	}
	


	public void onFetchComplete(List<Transaction> data) {
		 // dismiss the progress dialog
		transactionList = data;
        if(dialog != null)  dialog.dismiss();
        // create new adapter
        adapter = new TransLogAdapter(this, data);
        // set the adapter to list
        setListAdapter(adapter);
        
        //populate data on Map
        Iterator<Transaction> it = data.iterator();
        while(it.hasNext()){
        	Transaction transaction = it.next();
        	transactionMap.put(transaction.getTransLogId(), transaction);
        }
      
        registerForContextMenu(listView);
	}


	public void onFetchFailure(String msg) {
		// dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();  
		
	}
	
	
	
	/* BLUETOOTH ADAPTER */
	/*
	 * This will find a bluetooth printer device
	 */
	void findBT() {

		try {
			
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				//myLabel.setText("No bluetooth adapter available");
				Toast toast = Toast.makeText(getApplicationContext(), "No bluetooth adapter available", 1000);
			    toast.show();
			}

			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBluetooth = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					
					// MP300 is the name of the bluetooth printer device
					if (device.getName().equals(Constants.ANDROMEDA_BLUETOOTH_PRINTER_NAME)) {
						mmDevice = device;
						break;
					}
				}
			}
			//myLabel.setText("Bluetooth Device Found");
		} catch (NullPointerException e) {
			Toast toast = Toast.makeText(getApplicationContext(), "No bluetooth adapter available, null pointer", 1000);
		    toast.show();
		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(), "No bluetooth adapter available, exception", 1000);
		    toast.show();
		}
	}

	/*
	 * Tries to open a connection to the bluetooth printer device
	 */
	void openBT() throws IOException {
		try {
			// Standard SerialPortService ID
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();

			beginListenForData();

			//myLabel.setText("Bluetooth Opened");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * After opening a connection to bluetooth printer device, 
	 * we have to listen and check if a data were sent to be printed.
	 */
	void beginListenForData() {
		try {
			final Handler handler = new Handler();
			
			// This is the ASCII code for a newline character
			final byte delimiter = 10;

			stopWorker = false;
			readBufferPosition = 0;
			readBuffer = new byte[1024];
			
			workerThread = new Thread(new Runnable() {
				public void run() {
					while (!Thread.currentThread().isInterrupted()
							&& !stopWorker) {
						
						try {
							
							int bytesAvailable = mmInputStream.available();
							if (bytesAvailable > 0) {
								byte[] packetBytes = new byte[bytesAvailable];
								mmInputStream.read(packetBytes);
								for (int i = 0; i < bytesAvailable; i++) {
									byte b = packetBytes[i];
									if (b == delimiter) {
										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(readBuffer, 0,
												encodedBytes, 0,
												encodedBytes.length);
										final String data = new String(
												encodedBytes, "US-ASCII");
										readBufferPosition = 0;

										handler.post(new Runnable() {
											public void run() {
												//myLabel.setText(data);
											}
										});
									} else {
										readBuffer[readBufferPosition++] = b;
									}
								}
							}
							
						} catch (IOException ex) {
							stopWorker = true;
						}
						
					}
				}
			});

			workerThread.start();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This will send data to be printed by the bluetooth printer
	 */
	void sendData(String receipt) throws IOException {
		try {
			
			// the text typed by the user
			String msg = receipt;
			msg += "\n\n";
			
			mmOutputStream.write(msg.getBytes());
			
			// tell the user data were sent
			//yLabel.setText("Data Sent");
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Close the connection to bluetooth printer.
	 */
	void closeBT() throws IOException {
		try {
			stopWorker = true;
			mmOutputStream.close();
			mmInputStream.close();
			mmSocket.close();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
