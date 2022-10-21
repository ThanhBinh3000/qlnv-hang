package com.tcdt.qlnvhang.request.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.request.BaseRequest;
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
public class QlpktclhPhieuKtChatLuongRequestDto extends BaseRequest {

	private Long id;

	private Integer nam;

	private String maDvi;

	private String tenDvi;

	private String soPhieu;

	private String maQhns;

	private String idQdGiaoNvNh;

	private String soQdGiaoNvNh;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayQdGiaoNvNh;

	private String loaiVthh;

	private String tenLoaiVthh;

	private String cloaiVthh;

	private String moTaHangHoa;

	private String soHd;

	private String maDiemKho;

	private String maNhaKho;

	private String maNganKho;

	private String maLoKho;

	private String nguoiGiaoHang;

	private String cmtNguoiGiaoHang;

	private String donViGiaoHang;

	private String diaChi;

	private String bienSoXe;

	private Double soLuongDeNghiKt;

	private Double soLuongNhapKho;

	private String soChungThuGiamDinh;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayGdinh;

	private String tchucGdinh;

	private String ketLuan;

	private String kqDanhGia;

	private Long idDdiemGiaoNvNh;

	private List<QlpktclhKetQuaKiemTraRequestDto> ketQuaKiemTra;


}
