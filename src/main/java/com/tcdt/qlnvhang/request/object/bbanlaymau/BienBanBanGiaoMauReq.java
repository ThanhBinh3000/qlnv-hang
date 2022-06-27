package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanBanGiaoMauReq {
	private Long id;
	private Long qdgnvnxId;
	private Long bbLayMauId;
	private String soBienBan;
	private LocalDate ngayBanGiaoMau;

	private String maVatTuCha;
	private String maVatTu;
	private String tenDviBenNhan;
	private Integer soLuongMau;
	private String chiTieuKiemTra;
	private String tNiemPhongMauHang;

	private List<BienBanLayMauCtReq> chiTiets = new ArrayList<>();
}
