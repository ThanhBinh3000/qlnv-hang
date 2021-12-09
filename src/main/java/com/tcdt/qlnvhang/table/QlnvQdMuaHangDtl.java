package com.tcdt.qlnvhang.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_MUA_HANG_DTL")
@Data
public class QlnvQdMuaHangDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUA_HANG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUA_HANG_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_MUA_HANG_DTL_SEQ")
	private Long id;

	Long idHdr;
	String soDxuat;
	String maDvi;
	String soLuongDxuat;
}
