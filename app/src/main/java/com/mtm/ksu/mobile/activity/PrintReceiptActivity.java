		package com.mtm.ksu.mobile.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import com.mtm.ksu.mobile.common.Constants;
import com.mtm.ksu.mobile.service.BluetoothPrinterAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class PrintReceiptActivity extends Activity{
	final Context context = this;
	SharedPreferencesManager SharedPrefMgr;
	TextView strToPrintText;
	String receipt;
	static String MODE = "MULTIMODE";
	
	protected static final int OPTIONMENU_OPTION_PRINT = 1; 
	protected static final int OPTIONMENU_OPTION_EXIT = 2; 
	
	
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
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.print_receipt_item);
        
        SharedPrefMgr = new SharedPreferencesManager(this);
        
        strToPrintText = (TextView)findViewById(R.id.text_to_print);
       
        Bundle extras = getIntent().getExtras();
		if(extras != null){
			receipt = extras.getString("Receipt");
			MODE = extras.getString("MODE");
			
		}
		strToPrintText.setText(receipt);
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(Menu.NONE, OPTIONMENU_OPTION_PRINT, 0, "Print"); 
		 menu.add(Menu.NONE, OPTIONMENU_OPTION_EXIT, 1, "Main Menu");
		 
		 return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case OPTIONMENU_OPTION_PRINT:
			
				try {
					findBT();
					openBT();
					sendData(receipt);
					//closeBT();
				} catch (IOException ex) {
				}
				break;
	 		
			case OPTIONMENU_OPTION_EXIT:
				try {
					closeBT();
					
				} catch (IOException e) {
					//e.printStackTrace();
				}
				Intent intent = new Intent(context,MainMenuActivity.class);
				BluetoothPrinterAdapter.closeBluetooth();
				startActivity(intent);
				finish();
				break;
	 
		}
		return true;
	}
	
	/*
	 * This will find a bluetooth printer device
	 */
	void findBT() {

		try {
			
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				//myLabel.setText("No bluetooth adapter available");
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
					if (device.getName().equals(Constants.ANDROMEDA_BLUETOOTH_PRINTER_NAME)) {
						mmDevice = device;
						break;
					}
				}
			}
			//myLabel.setText("Bluetooth Device Found");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
