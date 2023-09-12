package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PhieuKnghiemCluongHangSearchReq extends BaseRequest {
	private Long id;
	private String soPhieu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayBanGiaoMauTu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayBanGiaoMauDen;
	private String maVatTuCha;
	private String soQdNhap;
	private String soBbBanGiao;
}
