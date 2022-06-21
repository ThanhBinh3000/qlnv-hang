package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "HH_DX_KHLCNT_THOP_DTL")
@Data
public class HhDxKhLcntThopDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_THOP_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_THOP_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_THOP_DTL_SEQ")
	private Long id;

	Long idThopHdr;
	Long idDxHdr;
	String maDvi;
	@Transient
	String tenDvi;
	String soDxuat;
	@Temporal(TemporalType.DATE)
	Date ngayDxuat;
	String tenDuAn;
	BigDecimal soLuong;
	BigDecimal donGia;
	BigDecimal tongTien;
	Long soGthau;
	String namKhoach;
	@Transient
	String trichYeu;

}
