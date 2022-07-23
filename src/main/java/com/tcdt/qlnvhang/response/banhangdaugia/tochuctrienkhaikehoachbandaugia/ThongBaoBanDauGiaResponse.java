package com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThongBaoBanDauGiaResponse {
	private Long id;
	private String maDonVi;
	private String capDonVi;
	private Integer namKeHoach;
	private Long loaiHangHoa;
	private Long soQuyetDinhPheDuyetBanDauGia;
	private String maThongBao;
	private String trichYeu;
	private String tenToChucDauGia;
	private String diaChi;
	private String dienThoai;
	private LocalDate thoiHanDangKyThamGiaDauGiaTuNgay;
	private LocalDate thoiHanDangKyThamGiaDauGiaDenNgay;
	private String luuYVeThoiGianDangKy;
	private String diaDiemMuaNopHoSoDangKy;
	private String dieuKienDangKyThamGiaDauGia;
	private String tienMuaHoSo;
	private String buocGiaCuaTungDonViTaiSan;
	private String buocGiaCuaTungDonViTaiSanGhiChu;
	private LocalDate thoiHanToChucXemTaiSanTuNgay;
	private LocalDate thoiHanToChucXemTaiSanDenNgay;
	private String luuYVeThoiGianXemTaiSan;
	private String diaDiemToChucXemTaiSan;
	private LocalDate thoiHanNopTienDatTruocTuNgay;
	private LocalDate thoiHanNopTienDatTruocDenNgay;
	private String luuYVeThoiGianNopTienDatTruoc;
	private String phuongThucThanhToan;
	private String donViThuHuong;
	private String soTaiKhoan;
	private String nganHang;
	private String chiNhanhNganHang;
	private LocalDate thoiGianToChucDauGiaTuNgay;
	private LocalDate thoiGianToChucDauGiaDenNgay;
	private String diaDiemToChucDauGia;
	private String hinhThucDauGia;
	private String phuongThucDauGia;
	private Long ghiChu;
	private String trangThai;
}
