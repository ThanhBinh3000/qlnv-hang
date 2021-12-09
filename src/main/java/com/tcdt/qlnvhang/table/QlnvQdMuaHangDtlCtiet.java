package com.tcdt.qlnvhang.table;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_MUA_HANG_DTL_CTIET")
@Data
public class QlnvQdMuaHangDtlCtiet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_QD_MUA_HANG_DTL_CTIET_SEQ")
	private Long id;

	Long idDtl;
	String maDvi;
	String dvts;
	BigDecimal soDxuat;
	BigDecimal soDuyet;
	String dviTinh;
	String donGia;
	BigDecimal tongTienCoc;
	BigDecimal tongTien;
}
