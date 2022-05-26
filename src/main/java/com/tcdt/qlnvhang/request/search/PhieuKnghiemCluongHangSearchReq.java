package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PhieuKnghiemCluongHangSearchReq extends BaseRequest {
	private String soPhieu;
	private String maDvi;
	private String maHhoa;
	private String maNgan;
	private String maKho;
	private String maLo;
	private LocalDate ngayKnghiemTuNgay;
	private LocalDate ngayKnghiemDenNgay;
}
