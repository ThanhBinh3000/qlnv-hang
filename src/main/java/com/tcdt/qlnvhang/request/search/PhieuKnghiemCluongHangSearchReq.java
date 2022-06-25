package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PhieuKnghiemCluongHangSearchReq extends BaseRequest {
	private String soPhieu;
	private LocalDate ngayBanGiaoMauTu;
	private LocalDate ngayBanGiaoMauDen;
	private String maVatTuCha;
	private String soQdNhap;
	private String soBbBanGiao;
	private String maDvi;
}
