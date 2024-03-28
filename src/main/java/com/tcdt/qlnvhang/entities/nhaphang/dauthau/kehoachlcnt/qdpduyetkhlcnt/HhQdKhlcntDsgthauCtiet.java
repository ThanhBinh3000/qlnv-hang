package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "HH_QD_KHLCNT_DSGTHAU_CTIET")
@Data
public class HhQdKhlcntDsgthauCtiet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	private Long id;

	private String maDvi;

	@Transient
	private String tenDvi;

	private String maDiemKho;

	@Transient
	private String tenDiemKho;

	private String diaDiemNhap;

	private BigDecimal soLuong;

	private BigDecimal donGia;
	private BigDecimal donGiaLastest;
	private BigDecimal donGiaTamTinh;

	private BigDecimal thanhTien;

	private Long idGoiThau;

	BigDecimal soLuongTheoChiTieu;

	BigDecimal soLuongDaMua;

	@Transient
	private List<HhQdKhlcntDsgthauCtietVt> children;
	@Transient
	private String thanhTienStr;
}