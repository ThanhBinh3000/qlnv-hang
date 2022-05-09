package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "HH_QD_GIAO_NVU_NHAPXUAT_DTL")
@Data
public class HhQdGiaoNvuNhapxuatDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_DTL_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_DTL_SEQ")
	private Long id;

	String maVthh;
	String tenVthh;
	String loaiNx;
	Date tuNgayThien;
	Date denNgayThien;
	Double soLuong;
	String maDvi;

	String donViTinh;

	@Transient
	String tenLoaiNx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhQdGiaoNvuNhapxuatHdr parent;

}
