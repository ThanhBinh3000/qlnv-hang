package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PhieuKnghiemCluongHangSearchReq extends BaseRequest {
	private String soPhieu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKnghiemTu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKnghiemDen;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMauTu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMauDen;

	private String maVatTuCha;
	private String soQdNhap;
	private String soBbBanGiao;
}
