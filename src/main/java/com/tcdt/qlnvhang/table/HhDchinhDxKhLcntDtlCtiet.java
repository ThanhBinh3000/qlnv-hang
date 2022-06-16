package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "HH_DC_DX_LCNT_DTL_CTIET")
@Data
public class HhDchinhDxKhLcntDtlCtiet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_CTIET_SEQ")
	private Long id;
	private Integer soLuong;
	private String tenDvi;
	private String maDvi;
	private Long idDtl;
}
