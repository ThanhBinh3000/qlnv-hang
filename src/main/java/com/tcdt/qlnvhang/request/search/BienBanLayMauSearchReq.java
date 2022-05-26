package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BienBanLayMauSearchReq extends BaseRequest {
	private String soQdinhGiaoNvuNhap;
	private String maKho;
	private String maNgan;
	private String maLo;
	private String maHhoa;
	private LocalDate ngayLapBbanTuNgay;
	private LocalDate ngayLapBbanDenNgay;
}
