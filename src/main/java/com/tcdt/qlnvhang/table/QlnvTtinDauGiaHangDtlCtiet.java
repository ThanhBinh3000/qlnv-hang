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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_DAUGIA_HANG_DTL_CTIET")
@Data
public class QlnvTtinDauGiaHangDtlCtiet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DAUGIA_HANG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DAUGIA_HANG_DTL_SEQ", allocationSize = 1, name = "QLNV_DAUGIA_HANG_DTL_SEQ")
	private Long id;

	String maDviThau;
	String tenDviThau;
	BigDecimal giaBothau;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dtl")
	@JsonBackReference
	private QlnvTtinDauGiaHangDtl parent;

}
