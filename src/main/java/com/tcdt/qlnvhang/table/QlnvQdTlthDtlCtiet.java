package com.tcdt.qlnvhang.table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "QLNV_QD_TLTH_DTL_CTIET")
@Data
public class QlnvQdTlthDtlCtiet implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_TLTH_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_TLTH_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_QD_TLTH_DTL_CTIET_SEQ")
	private Long id;
	String maDvi;
	String dvts;
	BigDecimal soDxuat;
	BigDecimal soDuyet;
	String dviTinh;
	String maKho;
	String maNgan;
	String maLo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dtl")
	@JsonBackReference
	private QlnvQdTlthDtl parent;
}
