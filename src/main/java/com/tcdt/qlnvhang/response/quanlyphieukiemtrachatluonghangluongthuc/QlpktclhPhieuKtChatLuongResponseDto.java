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

	private Long ngayKiemTra;

	private String nguoiTheoXe;

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

	private Long nguoiPheDuyet;

	private LocalDate ngayPheDuyet;

	private String trangThai;

	private String ketLuan;

	private String lyDoTuChoi;

	private String tenTrangThai;

	private List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTra;
}
