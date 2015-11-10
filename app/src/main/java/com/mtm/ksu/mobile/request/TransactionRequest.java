package com.mtm.ksu.mobile.request;

public class TransactionRequest {
	private String userId;
	private String kodeTrans;
	private String tanggalTrans;
	private String noRekening;
	private String namaNasabah;
	private String jumlahTrans;
    private String saldoTabAft;
	private String imei;
	
	//angsuran
	private String jumlahPokok;
	private String jumlahBunga;
	private String jumlahDenda;
    private String jumlahAdmin;
    private String totalAngs;
    private String totalBungaAdmin;
    private String angsKe;
    private String angsSisa;
    private String tglJt;
	
	//for printing info
	private String tipeTrans;
	private String noKuitansi;
	private String namaPerusahaan;
	
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getNamaPerusahaan() {
		return namaPerusahaan;
	}
	public void setNamaPerusahaan(String namaPerusahaan) {
		this.namaPerusahaan = namaPerusahaan;
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
	public String getTipeTrans() {
		return tipeTrans;
	}
	public void setTipeTrans(String tipeTrans) {
		this.tipeTrans = tipeTrans;
	}
	public String getNoKuitansi() {
		return noKuitansi;
	}
	public void setNoKuitansi(String noKuitansi) {
		this.noKuitansi = noKuitansi;
	}
	
	//ANGSURAN
	public String getJumlahPokok() {
		return jumlahPokok;
	}
	public void setJumlahPokok(String jumlahPokok) {
		this.jumlahPokok = jumlahPokok;
	}
	public String getJumlahBunga() {
		return jumlahBunga;
	}
	public void setJumlahBunga(String jumlahBunga) {
		this.jumlahBunga = jumlahBunga;
	}
	public String getJumlahDenda() {
		return jumlahDenda;
	}
	public void setJumlahDenda(String jumlahDenda) {
		this.jumlahDenda = jumlahDenda;
	}
    public String getJumlahAdmin() {
        return jumlahAdmin;
    }
    public void setJumlahAdmin(String jumlahAdmin) {
        this.jumlahAdmin = jumlahAdmin;
    }
    public String getAngsKe() {
        return angsKe;
    }
    public void setAngsKe(String angsKe) {
        this.angsKe = angsKe;
    }
    public String getAngsSisa() {
        return angsSisa;
    }
    public void setAngsSisa(String angsSisa) {
        this.angsSisa = angsSisa;
    }
    public String getTotalBungaAdmin() {
        return totalBungaAdmin;
    }
    public void setTotalBungaAdmin(String totalBungaAdmin) {
        this.totalBungaAdmin = totalBungaAdmin;
    }
    public String getTglJt() {
        return tglJt;
    }
    public void setTglJt(String tglJt) {
        this.tglJt = tglJt;
    }
    public String getTotalAngs() {
        return totalAngs;
    }
    public void setTotalAngs(String totalAngs) {
        this.totalAngs = totalAngs;
    }
}
