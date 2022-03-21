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
@Table(name = "QLNV_10.QLHDMVT_DS_GOI_THAU")
public class QlhdmvtDsGoiThau implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	private String ID;

	@Column(name = "STT")
	private String STT;

	@Column(name = "TEN_GOI_THAU")
	private String tenGoiThau;

	@Column(name = "SO_HIEU_GOI_THAU")
	private String soHieuGoiThau;

	@Column(name = "SO_LUONG")
	private String soLuong;

	@Column(name = "THUE_VAT")
	private String thueVat;

	@Column(name = "DON_GIA_TRUOC_THUE")
	private String donGiaTruocThue;

	@Column(name = "THUE")
	private String THUE;

	@Column(name = "GIA_TRU0C_THUE")
	private String giaTru0cThue;

	@Column(name = "GIA_SAU_THUE")
	private String giaSauThue;

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
