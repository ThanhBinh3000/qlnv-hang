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

	private String maVatTu;
	private String tenVatTu;
	private String maVatTuCha;
	private String tenVatTuCha;

	private String soBbanKthucNhap;
	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String trangThai;
	private String tenTrangThai;
	private String trangThaiDuyet;
	private String ldoTchoi;
	private List<KquaKnghiemRes> kquaKnghiem = new ArrayList<>();
	private Long soLuongMauHangKt;
	private String maDiemKho;
	private String tenDiemKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String maNganKho;
	private String tenNganKho;
	private String maNganLo;
	private String tenNganLo;

	private String maDvi;
	private String tenDvi;
	private String maDviCha;
	private String tenDviCha;
	private Long qdgnvnxId;
	private  String soQuyetDinhNhap;

	private Long bbBanGiaoMauId;
	private String soBbBanGiao;
	private LocalDate ngayBanGiaoMau;

	private String ketLuan;
	private String thuKho;
	private String ketQuaDanhGia;

}
