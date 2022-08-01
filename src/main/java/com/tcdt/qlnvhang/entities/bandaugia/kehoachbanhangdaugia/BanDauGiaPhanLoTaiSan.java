package com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = BanDauGiaPhanLoTaiSan.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BanDauGiaPhanLoTaiSan {
	public static final String TABLE_NAME = "BH_DG_KH_PHAN_LO_TAI_SAN";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ", allocationSize = 1, name = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "STT")
	private Long stt;

	@Column(name = "MA_DIEM_KHO")
	private String maDiemKho;

	@Column(name = "MA_NGAN_KHO")
	private String maNganKho;

	@Column(name = "MA_LO_KHO")
	private String maLoKho;

	@Column(name = "CHUNG_LOAI_HH")
	private String chungLoaiHh;

	@Column(name = "MA_DV_TAI_SAN")
	private String maDvTaiSan;

	@Column(name = "SO_LUONG")
	private BigDecimal soLuong;

	@Column(name = "DON_GIA")
	private BigDecimal donGia;

	@Column(name = "MA_CHI_CUC")
	private String maChiCuc;

	@Column(name = "BH_DG_KEHOACH_ID")
	private Long bhDgKehoachId;

	@Column(name = "GIA_KHOI_DIEM")
	private BigDecimal giaKhoiDiem;

	@Column(name = "SO_TIEN_DAT_TRUOC")
	private BigDecimal soTienDatTruoc;

	@Column(name = "DON_VI_TINH")
	private String donViTinh;

	// Bien ban ban dau gia
	@Column(name = "BB_BAN_DAU_GIA_ID")
	private Long bbBanDauGiaId;

	@Column(name = "SO_LAN_TRA_GIA")
	private Integer soLanTraGia;

	@Column(name = "DON_GIA_CAO_NHAT")
	private BigDecimal donGiaCaoNhat;

	@Column(name = "THANH_TIEN")
	private BigDecimal thanhTien;

	@Column(name = "TRA_GIA_CAO_NHAT")
	private String traGiaCaoNhat;

	// Quyet dinh Phe duyet ket qua ban dau gia
	@Column(name = "DON_GIA_TRUNG_DAU_GIA")
	private BigDecimal donGiaTrungDauGia;

	@Column(name = "TRUNG_DAU_GIA")
	private String trungDauGia;

	@Column(name = "QD_PHE_DUYET_KQBDG_ID")
	private String qdPheDuyetKqbdgId;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;


}
