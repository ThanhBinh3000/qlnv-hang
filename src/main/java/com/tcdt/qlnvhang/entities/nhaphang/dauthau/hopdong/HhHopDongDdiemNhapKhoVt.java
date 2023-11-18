package com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_HOP_DONG_DDIEM_NHAP_KHO_VT")
@Data
public class HhHopDongDdiemNhapKhoVt {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_HD_DDIEM_NHAP_KHO_VT_SEQ")
	@SequenceGenerator(sequenceName = "HH_HD_DDIEM_NHAP_KHO_VT_SEQ", allocationSize = 1, name = "HH_HD_DDIEM_NHAP_KHO_VT_SEQ")
	private Long id;

	private String maDvi;

	@Transient
	private String tenDvi;
	@Transient
	private String diaDiemNhap;

	private BigDecimal soLuong;

	private Long idHdongDdiemNkho;

	// preview
	@Transient
	private BigDecimal tongThanhTien;
	@Transient
	private Double donGia;
}