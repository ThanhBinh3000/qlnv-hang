package com.tcdt.qlnvhang.response.bbanlaymau;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanLayMauRes {
	private Long id;
	private String tenHhoa;
	private String ccuQdinhGiaoNvuNhap;
	private String maKho;
	private String tenKho;
	private String maLo;
	private String tenLo;
	private String maNgan;
	private String tenNgan;
	private String soBban;
	private LocalDate ngayLapBban;
	private String tenDviNhan;
	private String tenDdienNhan;
	private String cvuDdienNhan;
	private String tenDviCcap;
	private String tenDdienCcap;
	private String cvuDdienCcap;
	private String canCu;
	private String ddiemKtra;
	private String sluongLmau;
	private String pphapLayMau;
	private String ctieuKtra;
	private String kquaNiemPhongMau;
	private String trangThai;

	private String maDonVi;
	private String maQHNS;
	private String soHd;
	private LocalDate ngayLayMau;
	private String ddiemLayMau;
	private String ddienCucDtruNnuoc;
	private String tphongKthuatBquan;
	private String ddienBenNhan;
	private LocalDate ngayBgiaoMau;
	private String ddienCucDtruBenGiao;
	private String ddienDviTchucBenNhan;
	private String tinhTrang;
	private Integer soTrang;
}
