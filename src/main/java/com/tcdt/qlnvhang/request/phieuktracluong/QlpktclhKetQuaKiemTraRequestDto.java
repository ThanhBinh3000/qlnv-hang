package com.tcdt.qlnvhang.request.phieuktracluong;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QlpktclhKetQuaKiemTraRequestDto {

	private Long id;

	private String tenTchuan;

	private String chiSoNhap;

	private String ketQuaKiemTra;

	private String phuongPhap;

	private String kieu;

	private String trangThai;
	private String danhGia;

	private Long phieuKtChatLuongId;
}
