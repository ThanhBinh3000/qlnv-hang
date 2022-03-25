package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHDMVT_PHU_LUC_HOP_DONG")
public class QlhdmvtPhuLucHopDong {
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "STT")
	private String stt;

	@Column(name = "SO_PHU_LUC")
	private String soPhuLuc;

	@Column(name = "NGAY_KY")
	private java.sql.Date ngayKy;

	@Column(name = "NGAY_HIEU_LUC")
	private java.sql.Date ngayHieuLuc;

	@Column(name = "VE_VIEC")
	private String veViec;

	@Column(name = "NGAY_TAO")
	private java.sql.Date ngayTao;

	@Column(name = "NGUOI_TAO_ID")
	private Long nguoiTaoId;

	@Column(name = "NGAY_SUA")
	private java.sql.Date ngaySua;

	@Column(name = "NGUOI_SUA_ID")
	private Long nguoiSuaId;

	@Column(name = "QLHD_MUA_VAT_TU_ID")
	private Long qlhdMuaVatTuId;
}
