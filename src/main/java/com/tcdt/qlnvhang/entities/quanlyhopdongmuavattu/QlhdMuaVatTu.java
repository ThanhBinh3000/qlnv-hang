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
@Table(name = "QLNV_10.QLHD_MUA_VAT_TU")
public class QlhdMuaVatTu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	private String ID;

	@Column(name = "CAN_CU_ID")
	private String canCuId;

	@Column(name = "DV_TRUNG_THAU_ID")
	private String dvTrungThauId;

	@Column(name = "SO_HOP_DONG")
	private String soHopDong;

	@Column(name = "TEN_HOP_DONG")
	private String tenHopDong;

	@Column(name = "DON_VI_TRUNG_THAU_ID", nullable = false)
	private String donViTrungThauId;

	@Column(name = "NGAY_KY")
	private LocalDate ngayKy;

	@Column(name = "NGAY_TAO")
	private LocalDate ngayTao;

	@Column(name = "NGUOI_TAO_ID")
	private String nguoiTaoId;

	@Column(name = "NGAY_SUA")
	private LocalDate ngaySua;

	@Column(name = "NGUOI_SUA_ID")
	private String nguoiSuaId;

	@Column(name = "THONG_TIN_CHUNG_ID")
	private String thongTinChungId;

}
