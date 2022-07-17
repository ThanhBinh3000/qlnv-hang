package com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongResponseDto extends SoBienBanPhieuRes {
	private Long id;

	private String soPhieu;

	private LocalDate ngayKiemTra;

	private String nguoiTheoXe;

	private String diaChi;

	private Long hopDongId;

	private Long quyetDinhNhapId;

	private String soQuyetDinhNhap;

	private String maVatTu;
	private String maVatTuCha;

	private String tenVatTu;
	private String tenVatTuCha;

	private Double khoiLuong;

	private String soChungThuGiamDinh;

	private String bienSoXe;

	private String soPhieuAnToanThucPham;

	private String maDvi;
	private String tenDvi;
	private String maQhns;

	private String ketLuan;
	private String kqDanhGia;
	private String lyDoTuChoi;

	private String trangThai;

	private String tenTrangThai;
	private String trangThaiDuyet;

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

	private String khoiLuongDeNghiKt;

	private String soHopDong;

	private Date ngayHopDong;

	private String tenLoaiVthh;
	private String loaiVthh;

	private List<QlpktclhKetQuaKiemTraResponseDto> ketQuaKiemTra;
}
