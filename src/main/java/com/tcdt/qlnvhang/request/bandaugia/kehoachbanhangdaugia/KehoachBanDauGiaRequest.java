package com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
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
public class KehoachBanDauGiaRequest {
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
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
	private List<BanDauGiaDiaDiemGiaoNhanRequest> diaDiemGiaoNhanList = new ArrayList<>();
	private List<BanDauGiaPhanLoTaiSanRequest> phanLoTaiSanList = new ArrayList<>();
	private String thoiGianKyHopDongGhiChu;
	private String thoiHanThanhToanGhiChu;
	private String ThoiHanGiaoNhanGhiChu;
}
