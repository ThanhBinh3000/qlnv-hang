package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "HH_DC_DX_LCNT_DSGTHAU_CTIET")
@Data
public class HhDchinhDxKhLcntDsgthauCtiet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_CTIET_SEQ")
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
	private BigDecimal donGiaTamTinh;

	private BigDecimal thanhTien;
	private BigDecimal soLuongTheoChiTieu;
	private BigDecimal soLuongDaMua;

	private Long idGoiThau;
	@Transient
	private List<HhDchinhDxKhLcntDsgthauCtietVt> children = new ArrayList<>();

}
