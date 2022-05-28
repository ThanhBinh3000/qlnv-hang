package com.tcdt.qlnvhang.request.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
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

	@NotNull
	private Long hopDongId;

	@NotNull
	private Long quyetDinhNhapId;

	@NotNull
	private String maNganKho;
	private String tenNganKho;

	private String maNganLo;

	private String tenNganLo;

	@NotNull
	private String maDiemKho;

	private String tenDiemKho;
	@NotNull
	private String maNhaKho;

	private String tenNhaKho;

	@NotNull
	private Long maHangHoa;

	private String tenHangHoa;

	@NotNull
	private Double khoiLuong;

	private Long soChungThuGiamDinh;

	@NotNull
	private String bienSoXe;

	private String soPhieuAnToanThucPham;

	@NotNull
	private String maDonVi;

	private String maQhns;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayGdinh;

	private String tchucGdinh;

	private Long nguoiPheDuyet;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayPheDuyet;

	private String trangThai;

	@NotNull
	private String ketLuan;

	private String lyDoTuChoi;

	private List<QlpktclhKetQuaKiemTraRequestDto> ketQuaKiemTra;
}
