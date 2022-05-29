package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanBanGiaoMauReq {
	private Long id;
	private String maHhoa;
	private String ccuQdinhGiaoNvuNhap;
	private String maKho;
	private String maLo;
	private String maNgan;
	private String soBban;
	private String soBbanLayMau;
	private LocalDate ngayLapBban;
	private String maDviNhan;
	private String tenDdienNhan;
	private String cvuDdienNhan;
	private String maDviCcap;
	private String tenDdienCcap;
	private String cvuDdienCcap;
	private String canCu;
	private String ddiemKtra;
	private String sluongLmau;
	private String pphapLayMau;
	private String ctieuKtra;
	private String kquaNiemPhongMau;

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
