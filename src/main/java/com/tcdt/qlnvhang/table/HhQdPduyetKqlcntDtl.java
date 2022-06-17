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
@Table(name = HhQdPduyetKqlcntDtl.TABLE_NAME)
@Data
public class HhQdPduyetKqlcntDtl implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_PDUYET_KQLCNT_DTL";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhQdPduyetKqlcntDtl.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhQdPduyetKqlcntDtl.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhQdPduyetKqlcntDtl.TABLE_NAME + "_SEQ")
	private Long id;

	String shgt;
	String tenGthau;
	String diaDiem;
	BigDecimal soLuong;
	BigDecimal giaGthau;
	String tenDvi;
	String loaiHd;
	BigDecimal donGia;
	BigDecimal vat;
	BigDecimal dgiaSauVat;
	BigDecimal donGiaHd;
	BigDecimal vatHd;
	BigDecimal dgiaHdSauVat;
	Long idGoiThau;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhQdPduyetKqlcntHdr parent;

}
