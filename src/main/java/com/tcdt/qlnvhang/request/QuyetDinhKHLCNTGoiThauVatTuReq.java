package com.tcdt.qlnvhang.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuyetDinhKHLCNTGoiThauVatTuReq {
	private Long id;
	private String tenGoiThau;
	private BigDecimal giaGoiThau;
	private Long nguonVonId;
	private Long hinhThucLcntId;
	private Long phuongThucLcntId;
	private String tgBatDau;
	private String loaiHopDongId;
	private String tgThHopDong;
}
