package com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = BhQdPheDuyetKhbdgCt.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhQdPheDuyetKhbdgCt {
	public static final String TABLE_NAME = "BH_QD_PHE_DUYET_KHBDG_CT";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ")
	@SequenceGenerator(sequenceName = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ", allocationSize = 1, name = "BH_QD_PHE_DUYET_KHBDG_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "BH_DG_KE_HOACH_ID")
	private Long bhDgKeHoachId;

	@Column(name = "BH_QD_PHE_DUYET_KHBDG_ID")
	private Long quyetDinhPheDuyetId;

	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "NGAY_KY")
	private LocalDate ngayKy;

	@Column(name = "TRICH_YEU")
	private String trichYeu;

	@Column(name = "SO_LUONG_DV_TAI_SAN")
	private BigDecimal soLuongDvTaiSan;

	@Column(name = "GIA_KHOI_DIEM")
	private BigDecimal giaKhoiDiem;

	@Column(name = "KHOAN_TIEN_DAT_TRUOC")
	private BigDecimal khoanTienDatTruoc;

	@Column(name = "SO_KE_HOACH")
	private String soKeHoach;

	@Transient
	List<BhQdPheDuyetKhBdgThongTinTaiSan> thongTinTaiSans;
}
