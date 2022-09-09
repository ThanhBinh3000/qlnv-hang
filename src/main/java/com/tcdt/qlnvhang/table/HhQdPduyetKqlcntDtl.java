package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

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

//	String shgt;
	String tenGthau;
	BigDecimal soLuong;
	BigDecimal tongTien;
//	String tenDvi;
	String loaiHdong;
	@Transient
	String tenLoaiHdong;
	BigDecimal donGia;
	BigDecimal vat;
//	BigDecimal dgiaSauVat;
//	BigDecimal donGiaHd;
//	BigDecimal vatHd;
//	BigDecimal dgiaHdSauVat;
	Long idGoiThau;
	Long idQdPdHdr;
	String loaiVthh;
	@Transient
	String tenLoaiVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	Long idNhaThau;
	@Transient
	String tenNhaThau;
	Integer trungThau;
	String lyDoHuy;
	Integer tgianThienHd;
	BigDecimal donGiaTrcVat;

}
