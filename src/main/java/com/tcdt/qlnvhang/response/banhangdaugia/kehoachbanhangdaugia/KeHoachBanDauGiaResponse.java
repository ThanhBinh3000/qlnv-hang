package com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeHoachBanDauGiaResponse {
	private Long id;
	private String trangThai;
	private String trangThaiTh;
	private Integer namKeHoach;
	private String soKeHoach;
	private String trichYeu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLapKeHoach;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKy;
	private String loaiVthh;
	private String cloaiVthh;
	private String moTaHangHoa;
	private Long qdGiaoChiTieuId;
	private String tieuChuanChatLuong;
	private String loaiHopDong;
	private String thoiGianKyHd;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate tgDkTcTuNgay;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate tgDkTcDenNgay;
	private String thoiHanThanhToan;
	private String phuongThucThanhToan;
	private String thongBaoKhBdg;
	private String phuongThucGiaoNhan;
	private BigDecimal thoiHanGiaoNhan;
	private String maDv;
	private String capDv;
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();
	private List<BanDauGiaDiaDiemGiaoNhanResponse> diaDiemGiaoNhanList = new ArrayList<>();
	private List<BanDauGiaPhanLoTaiSanResponse> phanLoTaiSanList = new ArrayList<>();
	private String tenTrangThai;
	private String tenTrangThaiTh;
	private String soQuyetDinhGiaoChiTieu;
	private String soQuyetDinhPheDuyet;
	private String thoiGianKyHopDongGhiChu;
	private String thoiHanThanhToanGhiChu;
	private String ThoiHanGiaoNhanGhiChu;
	private BigDecimal tongSoLuongDonViTaiSan;
	private BigDecimal tongGiaKhoiDiem;
	private BigDecimal tongKhoanTienDatTruoc;
	private String tenDonVi;
}
