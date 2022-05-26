package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ThongTinDauThauVTSearchReq extends BaseRequest {
	private String soDxuat;
	private LocalDate ngayKyTuNgay;
	private LocalDate ngayKyDenNgay;
	private String soQdinh;
	private LocalDate ngayQdinhTuNgay;
	private LocalDate ngayQdinhDenNgay;
	private String tenDuAn;
}
