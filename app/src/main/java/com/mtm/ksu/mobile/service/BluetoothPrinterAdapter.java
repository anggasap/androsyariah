package com.mtm.ksu.mobile.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class BluetoothPrinterAdapter {
	private static BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mmSocket;
	private BluetoothDevice mmDevice;
	
	private OutputStream mmOutputStream;
	private InputStream mmInputStream;
	private Thread workerThread;
	
	private byte[] readBuffer;
	private int readBufferPosition;
	private int counter;
	private volatile boolean stopWorker;
	
	
	//singleton Object
	
	private static BluetoothPrinterAdapter mBluetoothPrinterAdapter;
	public static BluetoothPrinterAdapter getInstance(){
		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mBluetoothPrinterAdapter == null){
				mBluetoothPrinterAdapter = new BluetoothPrinterAdapter();	
		}
	
		return mBluetoothPrinterAdapter;
		
	}
	
	public boolean isEnabled(){
		return mBluetoothAdapter.isEnabled();
	}
	
	public BluetoothPrinterAdapter(){
		try {
			//mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
														.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					if (device.getName().equals(com.mtm.ksu.mobile.common.Constants.ANDROMEDA_BLUETOOTH_PRINTER_NAME)) {
						mmDevice = device;
						break;
					}
				}
				
				UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
				mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
				mmSocket.connect();
				mmOutputStream = mmSocket.getOutputStream();
				mmInputStream = mmSocket.getInputStream();
				
				beginListenForData();
			}
		
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	
	
	public void print(String text){
		try {
			
			// the text typed by the user
			String msg = text;
			msg += "\n\n";
			
			mmOutputStream.write(msg.getBytes());
			
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			stopWorker = true;
			try {
				mmOutputStream.close();
				mmInputStream.close();
				mmSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e){
				
			}
			
		}
		
	}
	
	public void close(){
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
		
		mBluetoothPrinterAdapter = null;
	}
	
	public static void closeBluetooth(){
		getInstance().close();
	}
	
	
	

}
