package com.tcdt.qlnvhang.table;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DX_KHLCNT_DSGTHAU_CTIET")
@Data
public class HhDxKhlcntDsgthauCtiet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	private Long id;

	@Transient
	private Long IdVirtual;

	private String maDvi;

	@Transient
	private String tenDvi;

	private String maDiemKho;

	@Transient
	private String tenDiemKho;

	private String diaDiemNhap;

	private BigDecimal soLuong;

	private BigDecimal donGia;

	private BigDecimal thanhTien;

	private Long idGoiThau;
}
