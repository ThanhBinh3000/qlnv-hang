package com.tcdt.qlnvhang.request.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class QlpktclhPhieuKtChatLuongRequestDto {
	private Long id;

	private Long soPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTra;

	private String nguoiGiaoHang;

	private String diaChi;

	private Long hopDongId;

	private Long quyetDinhNhapId;

	private Long maNganKho;

	private String tenNganKho;

	private Long maHangHoa;

	private String tenHangHoa;

	private Long khoiLuong;

	private Long soChungThuGiamDinh;

	private String bienSoXe;

	private String soPhieuAnToanThucPham;

	private Long fileDinhKemId;

	private String maDonVi;

	private String maQhns;

	private String maDiemKho;

	private String maNhaKho;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayGdinh;

	private String tchucGdinh;

	private Long nguoiPheDuyet;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayPheDuyet;

	private String trangThai;

	private String ketLuan;

	private String lyDoTuChoi;

	private List<QlpktclhKetQuaKiemTraRequestDto> ketQuaKiemTra;
}
