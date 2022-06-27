package com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhieuKnghiemCluongHangReq {
	private Long id;
	private Long qdgnvnxId;
	private Long bbBanGiaoMauId;
	private String soPhieu;
	private LocalDate ngayLayMau;
	private LocalDate ngayKnghiem;
	private String maVatTu;
	private String maVatTuCha;
	private String soBbanKthucNhap;
	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String ddiemBquan;
	private String trangThai;
	private String chiSoChatLuong;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;

	private String thuKho;
	private String ketLuan;
	private String ketQuaDanhGia;
	private List<KquaKnghiemReq> kquaKnghiem = new ArrayList<>();
}
