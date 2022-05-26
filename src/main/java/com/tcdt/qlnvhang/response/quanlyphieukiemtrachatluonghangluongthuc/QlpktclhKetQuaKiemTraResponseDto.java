package com.tcdt.qlnvhang.response.quanlyphieukiemtrachatluonghangluongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QlpktclhKetQuaKiemTraResponseDto {
	private Long id;

	private Long stt;

	private String tenChiTieu;

	private String tieuChuan;

	private String ketQuaKiemTra;

	private String phuongPhapXacDinh;

	private String trangThai;

	private Long phieuKtChatLuongId;
}
