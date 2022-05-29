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
@Table(name = "HH_QD_KHLCNT_DSGTHAU")
@Data
public class HhQdKhlcntDsgthau implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_DSGTHAU_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_DSGTHAU_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_DSGTHAU_SEQ")
	private Long id;

	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	String maDiemKho;
	String diaDiemNhap;
	BigDecimal donGia;
	BigDecimal thanhTien;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhQdKhlcntDtl parent;

}
