package com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_PHIEU_KT_CHAT_LUONG")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QlpktclhPhieuKtChatLuong extends TrangThaiBaseEntity implements Serializable {
	private static final long serialVersionUID = -5114185021472069821L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KT_CHAT_LUONG_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KT_CHAT_LUONG_SEQ", allocationSize = 1, name = "PHIEU_KT_CHAT_LUONG_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "SO_PHIEU")
	private String soPhieu;

	@Column(name = "MA_QHNS")
	private String maQhns;

	@Column(name = "NGAY_KIEM_TRA")
	private LocalDate ngayKiemTra;

	@Column(name = "NGUOI_GIAO_HANG")
	private String nguoiGiaoHang;

	@Column(name = "DIA_CHI")
	private String diaChi;

	@Column(name = "HOP_DONG_ID")
	private Long hopDongId;

	@Column(name = "QUYET_DINH_NHAP_ID")
	private Long quyetDinhNhapId;

	@Column(name = "MA_VAT_TU")
	private String maVatTu;

	@Column(name = "MA_VAT_TU_CHA")
	private String maVatTuCha;

	@Column(name = "SO_PHIEU_AN_TOAN_THUC_PHAM")
	private String soPhieuAnToanThucPham;

	@Column(name = "MA_DVI")
	private String maDvi;

	@Column(name = "MA_NGAN_KHO")
	private String maNganKho;

	@Column(name = "TEN_NGAN_KHO")
	private String tenNganKho;

	@Column(name = "MA_NGAN_LO")
	private String maNganLo;

	@Column(name = "TEN_NGAN_LO")
	private String tenNganLo;

	@Column(name = "MA_DIEM_KHO")
	private String maDiemKho;

	@Column(name = "TEN_DIEM_KHO")
	private String tenDiemKho;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;

	@Column(name = "TEN_NHA_KHO")
	private String tenNhaKho;

	@Column(name = "CAP_DVI")
	private String capDvi;

	@Column(name = "KHOI_LUONG_DE_NGHI_KT")
	private Double khoiLuongDeNghiKt;

	@Column(name = "KHOI_LUONG")
	private Double khoiLuong;

	@Column(name = "SO_CHUNG_THU_GIAM_DINH")
	private String soChungThuGiamDinh;

	@Column(name = "TCHUC_GDINH")
	private String tchucGdinh;

	@Column(name = "NGAY_GDINH")
	private LocalDate ngayGdinh;

	@Column(name = "BIEN_SO_XE")
	private String bienSoXe;

	@Column(name = "KET_LUAN")
	private String ketLuan;

	@Column(name = "KQ_DANH_GIA")
	private String kqDanhGia;

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;

	private Integer so;
	private Integer nam;

	@Transient
	private List<QlpktclhKetQuaKiemTra> ketQuaKiemTra = new ArrayList<>();
}
