package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuyetDinhKHLCNTGoiThauVatTuRes {
	private Long id;
	private String tenGoiThau;
	private BigDecimal giaGoiThau;
	private Long nguonVonId;
	private Long hinhThucLcntId;
	private Long phuongThucLcntId;
	private String tgBatDau;
	private String loaiHopDongId;
	private String tgThHopDong;
	private QuyetDinhKHLCNTVatTuRes quyetDinh;
}
