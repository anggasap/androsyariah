package com.mtm.ksu.mobile.common;

public class ActionConstants {
	public static String LOGIN_SERVICE_AUTHENTICATE = 
					     Constants.ANDROMEDA_ACTION_ROOT + "loginservice/authenticate";
	public static String REKENING_SERVICE_FETCH_DATA_LIST = 
	     				 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/getRekeningSearchList";
	public static String REKENING_SERVICE_FETCH_DATA_INFO = 
						 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/getRekeningInfo";
	public static String REKENING_SERVICE_SETOR_TABUNGAN = 
		 				 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/setorTabungan";
	public static String REKENING_SERVICE_TARIK_TABUNGAN = 
		 				 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/tarikTabungan";
    /*** Service Kredit ***/
    /***
     * START
     * Muncul konfirmasi setoran angsuran
     * Klik button simpan
     ***/
	public static String REKENING_SERVICE_SETOR_ANGSURAN = 
						 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/setorAngsuran";
    /***
     * END
     * Muncul konfirmasi setoran angsuran
     * Klik button simpan
     ***/
    /***
     * START
     * Isi nama nasabah kredit
     * Klik button cari
     ***/
	public static String REKENING_KREDIT_SERVICE_FETCH_DATA_LIST = 
		 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/getRekeningKreditSearchList";
    /***
     * END
     * Isi nama nasabah kredit
     * Klik button cari
     ***/
    /*Menangani event after focus NoRekeningText, show Nama Nasabah di NamaNasabahText*/
	public static String REKENING_KREDIT_SERVICE_FETCH_DATA_INFO = 
		 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/getRekeningKreditInfo";
    /*Menangani event after focus NoRekeningText, show Nama Nasabah di NamaNasabahText*/
    /*** End Service Kredit ***/
    public static String REKENING_SERVICE_TRANSLOG_LIST =
		 Constants.ANDROMEDA_ACTION_ROOT + "rekeningservice/getTransLogList";


}
