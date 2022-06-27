package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "HH_QD_KHLCNT_DTL")
@Data
public class HhQdKhlcntDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_DTL_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_DTL_SEQ")
	private Long id;
	private Long idQdHdr;
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
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	private List<HhQdKhlcntDsgthau> children = new ArrayList<>();

}
