package com.tcdt.qlnvhang.response.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanBanGiaoMauRes {
	private Long id;
	private Long qdgnvnxId;
	private String soQuyetDinhNhap;
	private Long bbLayMauId;
	private String soBbLayMau;
	private String soBienBan;
	private LocalDate ngayBanGiaoMau;

	private String maVatTuCha;
	private String tenVatTuCha;
	private String maVatTu;
	private String tenVatTu;
	private String tenDviBenNhan;
	private Integer soLuongMau;
	private String chiTieuKiemTra;
	private String ttNiemPhongMauHang;

	private String maDvi;
	private String tenDvi;

	private String lyDoTuChoi;
	private String trangThai;
	private String tenTrangThai;
	private String trangThaiDuyet;

	private List<BienBanBanGiaoMauCtRes> chiTiets = new ArrayList<>();
}
