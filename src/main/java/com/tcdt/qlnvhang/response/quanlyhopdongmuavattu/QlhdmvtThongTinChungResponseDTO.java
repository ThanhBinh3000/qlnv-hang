package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlhdmvtThongTinChungResponseDTO {
	private Long id;
	private LocalDate ngayHieuLucTuNgay;
	private Long ngayHieuLucDenNgay;
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
	private QlhdmvtTtChuDauTuResponseDTO thongTinChuDauTu;
	private QlhdmvtTtDonViCcResponseDTO donViCungCap;
}
