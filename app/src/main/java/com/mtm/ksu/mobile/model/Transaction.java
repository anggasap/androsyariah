package com.mtm.ksu.mobile.model;

public class Transaction {
	private String userId;
	private String kodeTrans;
	private String tanggalTrans;
	private String noRekening;
	private String namaNasabah;
	private String jumlahTrans;
    private String saldoTabAft;
	private String serverResponseStatus;
	private String serverResponseMessage;
	
	private String jumlahPokok;
	private String jumlahBunga;
	private String jumlahDenda;
    private String jumlahAdmin;
    private String totalAngs;
	//return
	private String noKuitansi;
	
	//log
	private String transLogId;
	private String transType;
	
	
	public String getJumlahPokok() {
		return jumlahPokok;
	}
	public void setjumlahPokok(String jumlahPokok) {
		this.jumlahPokok = jumlahPokok;
	}
	public String getJumlahBunga() {
		return jumlahBunga;
	}
	public void setjumlahBunga(String jumlahBunga) {
		this.jumlahBunga = jumlahBunga;
	}
	public String getJumlahDenda() {
		return jumlahDenda;
	}
	public void setjumlahDenda(String jumlahDenda) {
		this.jumlahDenda = jumlahDenda;
	}
    public String getJumlahAdmin() {
        return jumlahAdmin;
    }
    public void setjumlahAdmin(String jumlahAdmin) {
        this.jumlahAdmin = jumlahAdmin ;
    }
    public String getTotalAngs() {
        return totalAngs;
    }
    public void setTotalAngs(String totalAngs) {
        this.totalAngs = totalAngs;
    }
	
	
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransLogId() {
		return transLogId;
	}
	public void setTransLogId(String transLogId) {
		this.transLogId = transLogId;
	}
	public String getNoKuitansi() {
		return noKuitansi;
	}
	public void setNoKuitansi(String noKuitansi) {
		this.noKuitansi = noKuitansi;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKodeTrans() {
		return kodeTrans;
	}
	public void setKodeTrans(String kodeTrans) {
		this.kodeTrans = kodeTrans;
	}
	public String getTanggalTrans() {
		return tanggalTrans;
	}
	public void setTanggalTrans(String tanggalTrans) {
		this.tanggalTrans = tanggalTrans;
	}
	public String getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(String noRekening) {
		this.noRekening = noRekening;
	}
	public String getNamaNasabah() {
		return namaNasabah;
	}
	public void setNamaNasabah(String namaNasabah) {
		this.namaNasabah = namaNasabah;
	}
	public String getJumlahTrans() {
		return jumlahTrans;
	}
	public void setJumlahTrans(String jumlahTrans) {
		this.jumlahTrans = jumlahTrans;
	}
    public String getSaldoTabAft() {
        return saldoTabAft;
    }
    public void setSaldoTabAft(String saldoTabAft) {
        this.saldoTabAft = saldoTabAft;
    }
	public String getServerResponseStatus() {
		return serverResponseStatus;
	}
	public void setServerResponseStatus(String serverResponseStatus) {
		this.serverResponseStatus = serverResponseStatus;
	}
	public String getServerResponseMessage() {
		return serverResponseMessage;
	}
	public void setServerResponseMessage(String serverResponseMessage) {
		this.serverResponseMessage = serverResponseMessage;
	}
	
	@Override
	public String toString() {
		return this.getNamaNasabah() == null ? "" :      
		     this.getNamaNasabah().toUpperCase();
	}
	
	
	
	
}
