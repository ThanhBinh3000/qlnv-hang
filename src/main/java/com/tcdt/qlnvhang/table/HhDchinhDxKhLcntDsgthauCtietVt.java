package com.tcdt.qlnvhang.table;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DC_DX_LCNT_DSGTHAU_CTIET_VT")
@Data
public class HhDchinhDxKhLcntDsgthauCtietVt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_CTIET_VT_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_CTIET_VT_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_CTIET_VT_SEQ")
	private Long id;
	private String maDvi;
	@Transient
	private String tenDvi;
	private BigDecimal soLuong;
	private Long idGoiThauCtiet;

}
