package com.tcdt.qlnvhang.response.xuathang.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.response.CommonResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhPhieuKnghiemCluongRes extends CommonResponse {
	private Long id;
	private String soPhieu;
	private LocalDate ngayLayMau;
	private LocalDate ngayKnghiem;

	private String maVatTu;
	private String tenVatTu;
	private String maVatTuCha;
	private String tenVatTuCha;

	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;

	private String maDiemKho;
	private String tenDiemKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String maNganKho;
	private String tenNganKho;
	private String maNganLo;
	private String tenNganLo;

	private Long qdgnvxId;
	private String soQuyetDinhXuat;

	private Long bbLayMauId;
	private String soBbLayMau;

	private String ketLuan;
	private String thuKho;
	private String ketQuaDanhGia;
	private String loaiVthh;

	private List<XhPhieuKnghiemCluongCtRes> cts = new ArrayList<>();
}
