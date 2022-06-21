package com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_PHIEU_KT_CHAT_LUONG")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlpktclhPhieuKtChatLuong extends BaseEntity {
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

	@Column(name = "MA_HANG_HOA")
	private String maHangHoa;

	@Column(name = "TEN_HANG_HOA")
	private String tenHangHoa;

	@Column(name = "SO_PHIEU_AN_TOAN_THUC_PHAM")
	private String soPhieuAnToanThucPham;

	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "NGUOI_PHE_DUYET_ID")
	private Long nguoiPheDuyetId;

	@Column(name = "NGAY_PHE_DUYET")
	private LocalDate ngayPheDuyet;

	@Column(name = "NGUOI_GUI_DUYET_ID")
	private Long nguoiGuiDuyetId;

	@Column(name = "NGAY_GUI_DUYET")
	private LocalDate ngayGuiDuyet;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "LY_DO_TU_CHOI")
	private String lyDoTuChoi;

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

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;

	@Transient
	private List<QlpktclhKetQuaKiemTra> ketQuaKiemTra = new ArrayList<>();
}
