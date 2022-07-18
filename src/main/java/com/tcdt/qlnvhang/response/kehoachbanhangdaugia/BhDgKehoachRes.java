package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

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
public class BhDgKehoachRes {
	private Long id;
	private String trangThai;
	private Integer namKeHoach;
	private String soKeHoach;
	private String trichYeu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLapKeHoach;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKy;
	private String loaiHangHoa;
	private Long qdGiaoChiTieuId;
	private String tieuChuanChatLuong;
	private BigDecimal soLuong;
	private BigDecimal khoanTienDatTruoc;
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
	private String loaiVatTuHangHoa;
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();
	private List<BhDgKhDiaDiemGiaoNhanRes> diaDiemGiaoNhanList = new ArrayList<>();
	private List<BhDgKhPhanLoTaiSanRes> phanLoTaiSanList = new ArrayList<>();
	private String tenTrangThai;
}
