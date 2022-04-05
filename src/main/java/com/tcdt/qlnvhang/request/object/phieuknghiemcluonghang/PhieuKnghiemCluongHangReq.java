package com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PhieuKnghiemCluongHangReq {
	private Long id;
	private String soPhieu;
	private LocalDate ngayLayMau;
	private LocalDate ngayKnghiem;
	private String maNgan;
	private String tenNgan;
	private String maKho;
	private String tenKho;
	private String maHhoa;
	private String tenHhoa;
	private String soBbanKthucNhap;
	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String ddiemBquan;
	private String trangThai;
}
