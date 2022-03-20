package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class DTVatTuGoiThauVTRes {
	private Long id;
	private Long ttdtVtId;
	private String tenGoiThau;
	private String maHhoa;
	private String tenHhoa;
	private BigDecimal soLuong;
	private BigDecimal donGia;
	private String pthucLcnt;
	private Integer tgianThienHdong;
	private String hthucHdong;
	private String ghiChu;
	private Integer stt;

	private Long soLuongDiaDiemNhap;
	private Long tongDiaDiemNhap;
	private List<DTGoiThauDiaDiemNhapVTRes> diaDiemNhap;
}
