package com.tcdt.qlnvhang.table;

import java.io.Serializable;

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
@Table(name = "HH_BB_NGHIEMTHU_KLST_DTL")
@Data
public class HhBbNghiemthuKlstDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BB_NGHIEMTHU_KLST_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_BB_NGHIEMTHU_KLST_DTL_SEQ", allocationSize = 1, name = "HH_BB_NGHIEMTHU_KLST_DTL_SEQ")
	private Long id;

	String noiDung;
	String dvt;
	Double soLuongTn;
	Double donGiaTn;
	Double thanhTienTn;
	Double soLuongQt;
	Double thanhTienQt;
	Double tongGtri;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhBbNghiemthuKlstHdr parent;

}
