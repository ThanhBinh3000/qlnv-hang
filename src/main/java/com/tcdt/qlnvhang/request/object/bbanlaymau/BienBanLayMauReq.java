package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanLayMauReq {
	private Long id;
	private String maHhoa;
	private String ccuQdinhGiaoNvuNhap;
	private String maKho;
	private String maLo;
	private String maNgan;
	private String soBban;
	private LocalDate ngayLapBban;
	private String maDviNhan;
	private String tenDdienNhan;
	private String cvuDdienNhan;
	private String maDviCcap;
	private String tenDdienCcap;
	private String cvuDdienCcap;
	private String canCu;
	private String ddiemKtra;
	private String sluongLMau;
	private String pphapLayMau;
	private String ctieuKtra;
	private String kquaNiemPhongMau;

	private String maDonVi;
	private String maQHNS;
	private String soHd;
	private String ngayLayMau;
	private String ddiemLayMau;
	private String ddienCucDtruNnuoc;
	private String tPhongKthuatBquan;
	private String ddienBenNhan;
	private String ngayBgiaoMau;
	private String ddienCucDtruBenGiao;
	private String ddienDviTchucBenNhan;
	private String tinhTrang;
	private String soTrang;

}
