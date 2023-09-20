package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "NH_PHIEU_KN_CLUONG_NK_CT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KquaKnghiemKhac {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KNGHIEM_CLUONG_CT_NK_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KNGHIEM_CLUONG_CT_NK_SEQ", allocationSize = 1, name = "PHIEU_KNGHIEM_CLUONG_CT_NK_SEQ")
	private Long id;

	@Column(name = "PHIEU_KNGHIEM_ID")
	private Long phieuKnghiemId;

	@Column(name = "TEN_TCHUAN")
	private String tenTchuan;

	@Column(name = "KET_QUA_KIEM_TRA")
	private String ketQuaKiemTra; // Ket qua phan tich

	@Column(name = "PHUONG_PHAP")
	private String phuongPhap;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "CHI_SO_NHAP")
	private String chiSoNhap;

	@Column(name = "KIEU")
	private String kieu;

	@Column(name = "DANH_GIA")
	private String danhGia;

	@Column(name = "MA_TCHUAN")
	private String maTchuan;
}
