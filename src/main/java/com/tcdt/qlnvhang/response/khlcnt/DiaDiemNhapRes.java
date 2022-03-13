package com.tcdt.qlnvhang.response.khlcnt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiaDiemNhapRes {
	private Long id;
	private Long goiThauId;
	private String maDonVi;
	private Double soLuongNhap;
	private Integer stt;
}
