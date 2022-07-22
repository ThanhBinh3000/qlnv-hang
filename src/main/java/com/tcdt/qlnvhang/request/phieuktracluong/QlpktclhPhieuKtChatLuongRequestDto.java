package com.tcdt.qlnvhang.request.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongRequestDto extends SoBienBanPhieuReq {
	private Long id;

	private String soPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTra;

	private String nguoiGiaoHang;

	private String diaChi;

	private Long hopDongId;

	private Long quyetDinhNhapId;

	private String maNganKho;
	private String tenNganKho;
	private String maNganLo;
	private String tenNganLo;
	private String maDiemKho;
	private String tenDiemKho;

	private String maNhaKho;

	private String tenNhaKho;

	private String maVatTu;
	private String maVatTuCha;

	private Double khoiLuong;

	private String soChungThuGiamDinh;

	private String bienSoXe;

	private String soPhieuAnToanThucPham;

	private String maDvi;

	private String maQhns;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayGdinh;

	private String tchucGdinh;

	private Long nguoiPheDuyet;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayPheDuyet;

	private String trangThai;

	private String ketLuan;
	private String kqDanhGia;

	private String lyDoTuChoi;

	private String loaiVthh;

	private Double khoiLuongDeNghiKt;

	private List<QlpktclhKetQuaKiemTraRequestDto> ketQuaKiemTra;
}
