package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanLayMauReq {
	private Long id;
	private Long qdgnvnxId;
	private Long bbNhapDayKhoId;
	private String soBienBan;
	private Long hopDongId;
	private LocalDate ngayHopDong;
	private String donViCungCap;
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;

	private String maVatTuCha;
	private String maVatTu;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;

	private List<BienBanLayMauCtReq> chiTiets = new ArrayList<>();
}
