package com.tcdt.qlnvhang.request.object.bbanlaymau;

import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanLayMauReq extends SoBienBanPhieuReq {

	private Long id;

	private Long qdgnvnxId;
	private Long bbNhapDayKhoId;
	private Long bbGuiHangId;
	private String soBienBan;
	private Long hopDongId;
	private LocalDate ngayHopDong;
	private String donViCungCap;
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;
	private String diaDiemBanGiao;

	private String maVatTuCha;
	private String maVatTu;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;
	private boolean ketQuaNiemPhong;
	private String loaiVthh;

	private List<BienBanLayMauCtReq> chiTiets = new ArrayList<>();
}
