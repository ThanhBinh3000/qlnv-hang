package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanBanGiaoMauSearchReq extends BaseRequest {
	private String soQuyetDinhNhap;
	private LocalDate ngayBanGiaoMauTu;
	private LocalDate ngayBanGiaoMauDen;
	private String soBienBan;
	private String soHopDong;
	private String maDvi;
	private String maVatTuCha;
}
