package com.mtm.ksu.mobile.activity;

import com.mtm.ksu.mobile.common.Constants;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesManager {
	Context mContext;

	SharedPreferences settings;
	SharedPreferences.Editor editor;

	public SharedPreferencesManager(Context mContext) {

		settings = mContext.getSharedPreferences(
				Constants.ANDROMEDA_MOBILE_KSU_PREFERENCES, 0);
		editor = settings.edit();

	}

	public String getMacAddress() {
		return settings.getString("MacAddress", null);
	}

	public void setMacAddress(String macAddress) {
		editor.putString("MacAddress", macAddress);
		editor.commit();
	}

	
	public String getImei() {
		return settings.getString("IMEI", null);
	}

	public void setImei(String imei) {
		editor.putString("IMEI", imei);
		editor.commit();
	}

	public String getCurrentPassword() {
		return settings.getString("CurrentPassword", null);
	}

	public void setCurrentPassword(String password) {
		editor.putString("CurrentPassword", password);
		editor.commit();
	}

	
	public void setUserName(String userName) {
		editor.putString("UserName", userName);
		editor.commit();
	}

	public String getUserName() {
		return settings.getString("UserName", null);
	}
	
	public void setTanggalOperasional(String tanggalOperasional) {
		editor.putString("TanggalOperasional", tanggalOperasional);
		editor.commit();
	}

	public String getTanggalOperasional() {
		return settings.getString("TanggalOperasional", null);
	}
	
	public void setUserId(String userId){
		editor.putString("UserId", userId);
		editor.commit();
	}
	
	public String getUserId(){
		return settings.getString("UserId", null);
	}
	
	public void setKodeTransSetor(String kodeTransSetor){
		editor.putString("KodeTransSetor", kodeTransSetor);
		editor.commit();
	}
	
	public String getKodeTransSetor(){
		return settings.getString("KodeTransSetor", null);
	}
	public void setKodeTransTarik(String kodeTransTarik){
		editor.putString("KodeTransTarik", kodeTransTarik);
		editor.commit();
	}
	
	public String getKodeTransTarik(){
		return settings.getString("KodeTransTarik", null);
	}
	public void setKodeTransAngsuran(String kodeTransAngsuran){
		editor.putString("KodeTransAngsuran", kodeTransAngsuran);
		editor.commit();
	}
	
	public String getKodeTransAngsuran(){
		return settings.getString("KodeTransAngsuran", null);
	}
	
	public void setCompanyName(String companyName){
		editor.putString("CompanyName", companyName);
		editor.commit();
	}
	
	public String getCompanyName(){
		return settings.getString("CompanyName", null);
	}
	
	
	
		public void clear(){
		editor.putString("MacAddress", null);
		editor.putString("CurrentPassword", null);
		editor.putString("IMEI", null);
		editor.putString("UserName", null);
		editor.putString("TanggalOperasional", null);
		editor.putString("KodeTransSetor", null);
		editor.putString("KodeTransTarik", null);
		editor.putString("KodeTransAngsuran", null);
		editor.putString("UserId", null);
		editor.putString("CompanyName", null);
		
		
	}
}
