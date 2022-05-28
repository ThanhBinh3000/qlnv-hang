package com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongResponseDto {
	private Long id;

	private Long soPhieu;

	private LocalDate ngayKiemTra;

	private String nguoiTheoXe;

	private String diaChi;

	private Long hopDongId;

	private Long quyetDinhNhapId;

	private Long maHangHoa;

	private String tenHangHoa;

	private Double khoiLuong;

	private Long soChungThuGiamDinh;

	private String bienSoXe;

	private String soPhieuAnToanThucPham;

	private String maDonVi;

	private Long nguoiPheDuyet;

	private LocalDate ngayPheDuyet;

	private String trangThai;

	private String ketLuan;

	private String lyDoTuChoi;

	private String tenTrangThai;

	private String maNganKho;

	private String tenNganKho;

	private String maNganLo;

	private String tenNganLo;

	private Long diemKhoId;

	private String maDiemKho;

	private String tenDiemKho;

	private String maNhaKho;

	private String tenNhaKho;

	private LocalDate ngayGdinh;

	private String nguoiGiaoHang;

	private String tchucGdinh;

	private List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTra;
}
