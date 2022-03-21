package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "QLNV_10.QLHDMVT_THONG_TIN_CHUNG")
public class QlhdmvtThongTinChung implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	private String ID;

	@Column(name = "NHL_TU_NGAY")
	private LocalDate nhlTuNgay;

	@Column(name = "NHL_DEN_NGAY")
	private String nhlDenNgay;

	@Column(name = "SO_NGAY_TH")
	private String soNgayTh;

	@Column(name = "LOAI_HANG_ID")
	private String loaiHangId;

	@Column(name = "TOC_DO_CC_TU_NGAY")
	private LocalDate tocDoCcTuNgay;

	@Column(name = "TOC_DO_CC_DEN_NGAY")
	private LocalDate tocDoCcDenNgay;

	@Column(name = "SO_NGAY_THEO_TEN_DO")
	private String soNgayTheoTenDo;

	@Column(name = "LOAI_HOP_DONG_ID")
	private String loaiHopDongId;

	@Column(name = "NUOC_SAN_XUAT_ID")
	private String nuocSanXuatId;

	@Column(name = "TIEU_CHUAN_CL")
	private String tieuChuanCl;

	@Column(name = "SO_LUONG")
	private String soLuong;

	@Column(name = "GIA_TRI_HD_TRUOC_THUE")
	private String giaTriHdTruocThue;

	@Column(name = "THUE_VAT")
	private String thueVat;

	@Column(name = "GIA_TRI_HD_SAU_THU")
	private String giaTriHdSauThu;

	@Column(name = "FILE_DINH_KEM_ID")
	private String fileDinhKemId;

	@Column(name = "CHU_DAU_TU_ID")
	private String chuDauTuId;

	@Column(name = "DV_CUNG_CAP_ID")
	private String dvCungCapId;

	@Column(name = "GHI_CHU")
	private String ghiChu;

	@Column(name = "NGAY_TAO")
	private LocalDate ngayTao;

	@Column(name = "NGUOI_TAO_ID")
	private String nguoiTaoId;

	@Column(name = "NGAY_SUA")
	private LocalDate ngaySua;

	@Column(name = "NGUOI_SUA_ID")
	private String nguoiSuaId;

}
