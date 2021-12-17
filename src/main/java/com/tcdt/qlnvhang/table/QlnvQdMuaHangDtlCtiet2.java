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
@Table(name = "QLNV_QD_MUA_HANG_DTL_CTIET")
@Data
public class QlnvQdMuaHangDtlCtiet2 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ")
	private Long id;
	
	String maDvi;
	String dvts;
	BigDecimal soDxuat;
	BigDecimal soDuyet;
	String dviTinh;
	String donGia;
	BigDecimal tongTienCoc;
	BigDecimal tongTien;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dtl")
	@JsonBackReference
	private QlnvQdMuaHangDtl2 parent;
	
}
