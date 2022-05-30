package com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhieuKnghiemCluongHangReq {
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
	private String chiSoChatLuong;

	private Long diemKhoId;
	private String maDiemKho;
	private String tenDiemKho;

	private Long nganLoId;
	private String maNganLo;
	private String tenNganLo;

	private Long nhaKhoId;
	private String maNhaKho;
	private String tenNhaKho;

	private String thuKho;
	private Long qdgnvnxId;
	private String ketLuan;
	private String ketQuaDanhGia;
	private List<KquaKnghiemReq> kquaKnghiem = new ArrayList<>();
}
