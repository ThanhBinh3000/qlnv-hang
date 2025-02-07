package com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = BhQdPheDuyetKhBdgThongTinTaiSan.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhQdPheDuyetKhBdgThongTinTaiSan implements Serializable {
	public static final String TABLE_NAME = "BH_QD_PD_KH_BDG_TT_TAI_SAN";
	private static final long serialVersionUID = 471290314456427399L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_QD_PD_KH_BDG_TT_TAI_SAN_SEQ")
	@SequenceGenerator(sequenceName = "BH_QD_PD_KH_BDG_TT_TAI_SAN_SEQ", allocationSize = 1, name = "BH_QD_PD_KH_BDG_TT_TAI_SAN_SEQ")
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
	private Long qdPheDuyetKqbdgId;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;

	@Column(name = "GHI_CHU")
	private String ghiChu;

	@Column(name = "QD_PD_KHBDG_CHI_TIET_ID")
	private Long qdPheDuyetKhbdgChiTietId;

	@Column(name = "THONG_BAO_BDG_ID")
	private Long thongBaoBdgId;

	@Column(name = "THONG_BAO_BDG_KT_ID")
	private Long thongBaoBdgKtId;
}
