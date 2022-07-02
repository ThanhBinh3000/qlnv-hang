package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class BienBanBanGiaoMauSearchReq extends BaseRequest {
	private String soQuyetDinhNhap;
	private LocalDate ngayBanGiaoMauTu;
	private LocalDate ngayBanGiaoMauDen;
	private String soBienBan;
	private String soHopDong;
	private String maVatTuCha;
}
