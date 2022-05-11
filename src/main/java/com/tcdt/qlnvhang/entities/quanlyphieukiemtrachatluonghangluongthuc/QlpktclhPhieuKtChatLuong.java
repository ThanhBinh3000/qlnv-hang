package com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "QLPKTCLH_PHIEU_KT_CHAT_LUONG")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlpktclhPhieuKtChatLuong extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SO_PHIEU")
	private Long soPhieu;

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

	@Column(name = "MA_NGAN_KHO")
	private Long maNganKho;

	@Column(name = "TEN_NGAN_KHO")
	private String tenNganKho;

	@Column(name = "MA_HANG_HOA")
	private Long maHangHoa;

	@Column(name = "TEN_HANG_HOA")
	private String tenHangHoa;

	@Column(name = "KHOI_LUONG")
	private Long khoiLuong;

	@Column(name = "SO_CHUNG_THU_GIAM_DINH")
	private Long soChungThuGiamDinh;

	@Column(name = "BIEN_SO_XE")
	private String bienSoXe;

	@Column(name = "SO_PHIEU_AN_TOAN_THUC_PHAM")
	private String soPhieuAnToanThucPham;

	@Column(name = "FILE_DINH_KEM_ID")
	private Long fileDinhKemId;

	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "NGUOI_PHE_DUYET")
	private Long nguoiPheDuyet;

	@Column(name = "NGAY_PHE_DUYET")
	private LocalDate ngayPheDuyet;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "KET_LUAN")
	private String ketLuan;

	@Column(name = "LY_DO_TU_CHOI")
	private String lyDoTuChoi;

	@Column(name = "MA_DIEM_KHO")
	private String maDiemKho;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;

	@Column(name = "NGAY_GDINH")
	private LocalDate ngayGdinh;

	@Column(name = "TCHUC_GDINH")
	private String tchucGdinh;

}
