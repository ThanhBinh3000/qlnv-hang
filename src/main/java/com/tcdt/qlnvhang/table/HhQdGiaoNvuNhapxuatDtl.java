package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "NH_QD_GIAO_NVU_NHAPXUAT_CT")
@Data
public class HhQdGiaoNvuNhapxuatDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_CT_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_CT_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_CT_SEQ")
	private Long id;

	@Column(name = "ID_HDR")
	private Long idHdr;

	@Column(name = "MA_DVI")
	String maDvi;

	@Column(name = "LOAI_VTHH")
	String loaiVthh;
	@Transient
	String tenLoaiVthh;

	@Column(name = "CLOAI_VTHH")
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;

	@Column(name = "DON_VI_TINH")
	String donViTinh;

	@Column(name = "SO_LUONG")
	BigDecimal soLuong;

	@Column(name = "TGIAN_NKHO")

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNkho;

	String loaiNx;

	Date tuNgayThien;

	Date denNgayThien;

	@Transient
	String tenLoaiNx;

	@Transient
	String tenDvi;


}
