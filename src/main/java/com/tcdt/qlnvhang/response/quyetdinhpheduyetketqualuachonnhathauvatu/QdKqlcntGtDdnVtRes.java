package com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class QdKqlcntGtDdnVtRes {
	private Long id;
	private Long goiThauId;
	private Long donViId;
	private String maDonVi;
	private BigDecimal soLuongNhap;
	private Integer stt;
}
