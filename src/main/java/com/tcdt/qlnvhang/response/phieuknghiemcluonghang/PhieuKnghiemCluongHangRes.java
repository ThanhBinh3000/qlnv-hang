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

	private String maHhoa;
	private String tenHhoa;
	private String soBbanKthucNhap;
	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String ddiemBquan;
	private String trangThai;
	private String tenTrangThai;
	private String trangThaiDuyet;
	private String ldoTchoi;
	private List<KquaKnghiemRes> kquaKnghiem = new ArrayList<>();
	private Long sluongKquaKnghiem;
	private Long tongSoKquaKnghiem;


	private Long diemKhoId;
	private String maDiemKho;
	private String tenDiemKho;

	private Long nganLoId;
	private String maNganLo;
	private String tenNganLo;

	private Long nhaKhoId;
	private String maNhaKho;
	private String tenNhaKho;
	private Long qdgnvnxId;

	private String ketLuan;
	private String thuKho;
}
