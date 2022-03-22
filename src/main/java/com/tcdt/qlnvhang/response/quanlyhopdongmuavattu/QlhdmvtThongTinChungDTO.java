package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdmvtThongTinChungDTO {
	private Long id;
	private LocalDate nhlTuNgay;
	private Long nhlDenNgay;
	private Long soNgayTh;
	private Long loaiHangId;
	private LocalDate tocDoCcTuNgay;
	private LocalDate tocDoCcDenNgay;
	private Long soNgayTheoTenDo;
	private Long loaiHopDongId;
	private Long nuocSanXuatId;
	private String tieuChuanCl;
	private Long soLuong;
	private BigDecimal giaTriHdTruocThue;
	private Integer thueVat;
	private BigDecimal giaTriHdSauThu;
	private Long fileDinhKemId;
	private String ghiChu;
	private QlhdmvtTtChuDauTuDTO thongTinChuDauTu;
	private QlhdmvtTtDonViCcDTO donViCungCap;
}
