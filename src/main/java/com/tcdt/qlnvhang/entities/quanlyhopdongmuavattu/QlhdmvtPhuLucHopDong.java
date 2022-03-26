package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

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
	private LocalDate ngayKy;

	@Column(name = "NGAY_HIEU_LUC")
	private LocalDate ngayHieuLuc;

	@Column(name = "VE_VIEC")
	private String veViec;

	@Column(name = "NGAY_TAO")
	private LocalDate ngayTao;

	@Column(name = "NGUOI_TAO_ID")
	private Long nguoiTaoId;

	@Column(name = "NGAY_SUA")
	private LocalDate ngaySua;

	@Column(name = "NGUOI_SUA_ID")
	private Long nguoiSuaId;

	@Column(name = "QLHD_MUA_VAT_TU_ID")
	private Long qlhdMuaVatTuId;
}
