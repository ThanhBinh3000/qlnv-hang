package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class HhQdPduyetKqlcntRes {

	private Long id;

	private String soQd;

	private Date ngayQd;

	private String trichYeu;

	private String tenGthau;

	private String statusGthau;

	private Long idNhaThau;

	private String tenNhaThau;

	private String lyDoHuy;

	private BigDecimal donGiaTrcVat;

	private Integer vat;

	private BigDecimal soLuong;

	private String loaiHdong;

	private Integer tgianThienHd;

	private String trangThai;

	private String tenHdong;

	public HhQdPduyetKqlcntRes(Long id, String soQd, Date ngayQd, String trichYeu, String tenGthau, String statusGthau, Long idNhaThau, String lyDoHuy, BigDecimal donGiaTrcVat, Integer vat, BigDecimal soLuong, String loaiHdong, Integer tgianThienHd, String trangThai) {
		this.id = id;
		this.soQd = soQd;
		this.ngayQd = ngayQd;
		this.trichYeu = trichYeu;
		this.tenGthau = tenGthau;
		this.statusGthau = statusGthau;
		this.idNhaThau = idNhaThau;
		this.lyDoHuy = lyDoHuy;
		this.donGiaTrcVat = donGiaTrcVat;
		this.vat = vat;
		this.soLuong = soLuong;
		this.loaiHdong = loaiHdong;
		this.tgianThienHd = tgianThienHd;
		this.trangThai = trangThai;
	}
}
