package com.tcdt.qlnvhang.request.kehoachbanhangdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhDgKehoachReq {
	private Long id;
	private String trangThai;
	private Integer namKeHoach;
	private String soKeHoach;
	private String trichYeu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	private LocalDate ngayLapKeHoach;
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKy;
	private String loaiHangHoa;
	private Long qdGiaoChiTieuId;
	private String tieuChuanChatLuong;
	private BigDecimal soLuong;
	private BigDecimal khoanTienDatTruoc;
	private String loaiHopDong;
	private String thoiGianKyHd;
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate tgDkTcTuNgay;
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
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
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
	private List<BhDgKhDiaDiemGiaoNhanReq> diaDiemGiaoNhanList = new ArrayList<>();
	private List<BhDgKhPhanLoTaiSanReq> phanLoTaiSanList = new ArrayList<>();
}
