package com.tcdt.qlnvhang.response.phieuknghiemcluonghang;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhieuKnghiemCluongHangRes {
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
	private String ldoTchoi;
	private List<KquaKnghiemRes> kquaKnghiem = new ArrayList<>();
	private Long sluongKquaKnghiem = 0L;
	private Long tongSoKquaKnghiem = 0L;
}
