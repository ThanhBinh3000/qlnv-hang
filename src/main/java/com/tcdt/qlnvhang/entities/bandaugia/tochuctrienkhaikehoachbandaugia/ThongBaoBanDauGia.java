package com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = ThongBaoBanDauGia.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThongBaoBanDauGia extends BaseEntity implements Serializable {

	public static final String TABLE_NAME = "BH_DG_THONG_BAO_BAN_DAU_GIA";
	private static final long serialVersionUID = 2279843539381075274L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_THONG_BAO_BDG_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_THONG_BAO_BDG_SEQ", allocationSize = 1, name = "BH_DG_THONG_BAO_BDG_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "CAP_DON_VI")
	private String capDonVi;

	@Column(name = "NAM_KE_HOACH")
	private Integer namKeHoach;

	@Column(name = "MA_VAT_TU_CHA")
	private String maVatTuCha;

	@Column(name = "MA_VAT_TU")
	private String maVatTu;

	@Column(name = "QD_PHE_DUYET_KHBDG_ID")
	private Long qdPheDuyetKhBdgId;

	@Column(name = "MA_THONG_BAO")
	private String maThongBao;

	@Column(name = "TRICH_YEU")
	private String trichYeu;

	@Column(name = "TEN_TO_CHUC_DG")
	private String tenToChucDauGia;

	@Column(name = "DIA_CHI")
	private String diaChi;

	@Column(name = "DIEN_THOAI")
	private String dienThoai;

	@Column(name = "THOI_HAN_DKTGDG_TU_NGAY")
	private LocalDate thoiHanDangKyThamGiaDauGiaTuNgay;

	@Column(name = "THOI_HAN_DKTGDG_DEN_NGAY")
	private LocalDate thoiHanDangKyThamGiaDauGiaDenNgay;

	@Column(name = "LUU_Y_VE_TG_DK")
	private String luuYVeThoiGianDangKy;

	@Column(name = "DIA_DIEM_MUA_NOP_HSDK")
	private String diaDiemMuaNopHoSoDangKy;

	@Column(name = "DIEU_KIEN_DKTGDG")
	private String dieuKienDangKyThamGiaDauGia;

	@Column(name = "TIEN_MUA_HS")
	private String tienMuaHoSo;

	@Column(name = "BUOC_GIA_CUA_DV_TS")
	private String buocGiaCuaTungDonViTaiSan;

	@Column(name = "BUOC_GIA_CUA_DV_TS_GHI_CHU")
	private String buocGiaCuaTungDonViTaiSanGhiChu;

	@Column(name = "TH_TC_XEM_TS_TU_NGAY")
	private LocalDate thoiHanToChucXemTaiSanTuNgay;

	@Column(name = "TH_TC_XEM_TS_DEN_NGAY")
	private LocalDate thoiHanToChucXemTaiSanDenNgay;

	@Column(name = "LUU_Y_VE_TG_XEM_TS")
	private String luuYVeThoiGianXemTaiSan;

	@Column(name = "DIA_DIEM_TC_XEM_TS")
	private String diaDiemToChucXemTaiSan;

	@Column(name = "TH_NOP_TIEN_DAT_TRUOC_TN")
	private LocalDate thoiHanNopTienDatTruocTuNgay;

	@Column(name = "TH_NOP_TIEN_DAT_TRUOC_DN")
	private LocalDate thoiHanNopTienDatTruocDenNgay;

	@Column(name = "LUU_Y_VE_TG_NOP_TIEN_DAT_TRUOC")
	private String luuYVeThoiGianNopTienDatTruoc;

	@Column(name = "PHUONG_THUC_THANH_TOAN")
	private String phuongThucThanhToan;

	@Column(name = "DON_VI_THU_HUONG")
	private String donViThuHuong;

	@Column(name = "SO_TAI_KHOAN")
	private String soTaiKhoan;

	@Column(name = "NGAN_HANG")
	private String nganHang;

	@Column(name = "CHI_NHANH_NGAN_HANG")
	private String chiNhanhNganHang;

	@Column(name = "TG_TO_CHUC_GD_TU_NGAY")
	private LocalDate thoiGianToChucDauGiaTuNgay;

	@Column(name = "TG_TO_CHUC_GD_DEN_NGAY")
	private LocalDate thoiGianToChucDauGiaDenNgay;

	@Column(name = "DIA_DIEM_TC_DAU_GIA")
	private String diaDiemToChucDauGia;

	@Column(name = "HINH_THUC_DG")
	private String hinhThucDauGia;

	@Column(name = "PHUONG_THUC_DG")
	private String phuongThucDauGia;

	@Column(name = "GHI_CHU")
	private String ghiChu;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;

	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
