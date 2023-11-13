package com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "HH_HOP_DONG_DTL")
@Data
public class HhHopDongDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_HOP_DONG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_HOP_DONG_DTL_SEQ", allocationSize = 1, name = "HH_HOP_DONG_DTL_SEQ")
	private Long id;
	Long idHdr;
	String shgt;
	String tenGthau;
	BigDecimal soLuong;
	BigDecimal donGiaVat;
	Long vat;
	BigDecimal giaTruocVat;
	BigDecimal giaSauVat;
	String type;
	String maDvi;
	@Transient
	String tenDvi;
	String diaDiemNhap;
	String trangThai;
	@Transient
	private String tongThanhTienStr;
	@Transient
	private List<HhHopDongDdiemNhapKho> children = new ArrayList<>();


}
