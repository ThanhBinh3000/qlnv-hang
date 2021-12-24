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
@Table(name = "QLNV_TTIN_CHAOGIA_DTL_CTIET")
@Data
public class QlnvTtinChaogiaDtlCtiet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TT_CHAOGIA_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TT_CHAOGIA_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_TT_CHAOGIA_DTL_CTIET_SEQ")
	private Long id;

	String diaChi;
	String tenDvi;
	BigDecimal soLuong;
	String dviTinh;
	BigDecimal giaChao;
	String ketQua;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dtl")
	@JsonBackReference
	private QlnvTtinChaogiaDtl parent;
}
