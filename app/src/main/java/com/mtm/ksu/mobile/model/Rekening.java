package com.mtm.ksu.mobile.model;

public class Rekening {

	private String noRekening;
	private String namaNasabah;
	private String saldo;
	public String getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(String noRekening) {
		this.noRekening = noRekening;
	}
	public String getNamaNasabah() {
		return namaNasabah;//
	}
	public void setNamaNasabah(String namaNasabah) {
		this.namaNasabah = namaNasabah;
	}
		
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	
	@Override
	public String toString() {
		return this.getNamaNasabah() == null ? "" :      
		     this.getNamaNasabah().toUpperCase();
	}
}
