package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld;

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
@Table(name = "NH_BB_NGHIEM_THU_CT")
@Data
public class HhBbNghiemthuKlstDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NGHIEM_THU_CT_SEQ")
	@SequenceGenerator(sequenceName = "BB_NGHIEM_THU_CT_SEQ", allocationSize = 1, name = "BB_NGHIEM_THU_CT_SEQ")
	private Long id;
	private Long hdrId;
	private String danhMuc;
	private String nhomHang;
	private String donViTinh;
	private String matHang;
	private String tenMatHang;
	private String donViTinhMh;
	private Double tongGiaTri;
	private Double soLuongTrongNam;
	private Double donGia;
	private Double thanhTienTrongNam;
	private Double soLuongNamTruoc;
	private Double thanhTienNamTruoc;
	private String type;
	private Boolean isParent;
	private String idParent;
}
