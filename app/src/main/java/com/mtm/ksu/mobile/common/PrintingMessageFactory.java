package com.mtm.ksu.mobile.common;

import com.mtm.ksu.mobile.request.TransactionRequest;

public class PrintingMessageFactory {
	public static String ReceiptMessage(TransactionRequest model){
	String receipt =   model.getNamaPerusahaan()+"\n";;
		//receipt = receipt +"(Persero)\n";;
		receipt = receipt +"-------------------------------\n\n";;

		String jumlahStr ="";
		if(Constants.PRINTING_TRANS_TYPE_SETOR_TABUNGAN.equals(model.getTipeTrans())){
            receipt = receipt +"BUKTI SETORAN TAB\n\n";;
            receipt = receipt +"No.Kuitansi:"+ model.getNoKuitansi()+"\n";;
            receipt = receipt +"Tanggal : "+ model.getTanggalTrans() +"\n";;
            receipt = receipt +"No. Rekening: "+ model.getNoRekening() +"\n";;
            receipt = receipt +"Nama : "+ model.getNamaNasabah() +"\n";;
            receipt = receipt +"Saldo awal : Rp. "+ model.getSaldoTabAwal() +"\n";;

			jumlahStr = "Jumlah Setoran :";
			receipt = receipt + jumlahStr +" Rp. "+ model.getJumlahTrans() +"\n";;
            receipt = receipt +"Saldo Akhir : Rp. "+ model.getSaldoTabAft() +"\n";;
		}else if (Constants.PRINTING_TRANS_TYPE_TARIK_TABUNGAN.equals(model.getTipeTrans())){
            receipt = receipt +"BUKTI PENARIKAN TAB\n\n";;
            receipt = receipt +"No.Kuitansi:"+ model.getNoKuitansi()+"\n";;
            receipt = receipt +"Tanggal : "+ model.getTanggalTrans() +"\n";;
            receipt = receipt +"No. Rekening: "+ model.getNoRekening() +"\n";;
            receipt = receipt +"Nama : "+ model.getNamaNasabah() +"\n";;
            receipt = receipt +"Saldo awal : Rp. "+ model.getSaldoTabAwal() +"\n";;
			jumlahStr = "Jumlah Penarikan :";
			receipt = receipt + jumlahStr +" Rp. "+ model.getJumlahTrans() +"\n";;
            receipt = receipt +"Saldo Akhir : Rp. "+ model.getSaldoTabAft() +"\n";;
		}else if(Constants.PRINTING_TRANS_TYPE_ANGSURAN.equals(model.getTipeTrans())){
            receipt = receipt +"BUKTI SETORAN ANGS\n\n";;
            receipt = receipt +"No.Kuitansi:"+ model.getNoKuitansi()+"\n";;
            receipt = receipt +"Tanggal : "+ model.getTanggalTrans() +"\n";;
            receipt = receipt +"No. Rekening: "+ model.getNoRekening() +"\n";;
            receipt = receipt +"Nama : "+ model.getNamaNasabah() +"\n";;
			//jumlahStr = "Jumlah Angsuran :";
			receipt = receipt + "Jumlah Pokok" +" Rp. "+ model.getJumlahPokok() +"\n";;
            /**
             * Untuk kasa arta struck jumlah bunga = jumlah bunga + jumlah admin
             */
            //receipt = receipt + "Jumlah Bunga" +" Rp. "+ model.getTotalBungaAdmin() +"\n";;

			receipt = receipt + "Jumlah Margin" +" Rp. "+ model.getJumlahBunga() +"\n";;
			receipt = receipt + "Jumlah Denda" +" Rp. "+ model.getJumlahDenda() +"\n";;
            receipt = receipt + "Jumlah Admin" +" Rp. "+ model.getJumlahAdmin() +"\n";;

            receipt = receipt + "Angs Ke : " +"  "+ model.getAngsKe() + "   Sisa Angs : " +"  "+ model.getAngsSisa() +"\n";;
            receipt = receipt +"Tgl JT : "+ model.getTglJt() +"\n";;
            receipt = receipt + "Total Angsuran" +" Rp. "+ model.getTotalAngs() +"\n";;
		}
		//receipt = receipt + jumlahStr +" Rp. "+ model.getJumlahTrans() +"\n";;
		//receipt = receipt +"-------------------------------\n\n";;

		receipt = receipt +"Debitur\n\n\n";;
		receipt = receipt +"Tanda Tangan";
		receipt = receipt + "\n";
		receipt = receipt +model.getNamaNasabah() +"\n\n" ;

		/*
		if(model.getUserName() != null){
			receipt = receipt +"Petugas Unit\n\n\n";;
			receipt = receipt +"Tanda Tangan";
			receipt = receipt + "\n";
			receipt = receipt +model.getUserName().toUpperCase();
		}
		*/
		receipt = receipt +"\n\n";;
		receipt = receipt +"Struk ini agar disimpan sebagai bukti setoran.";
		receipt = receipt +"\n";;
		return receipt;
	}
	
	
	
	}
