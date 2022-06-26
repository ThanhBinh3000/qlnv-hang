package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanLayMauSearchReq extends BaseRequest {
	private String soQuyetdinhNhap;
	private String soBienBan;
	private LocalDate ngayLayMauTu;
	private LocalDate ngayLayMauDen;
	private String maVatTuCha;
	private String maDvi;
}
