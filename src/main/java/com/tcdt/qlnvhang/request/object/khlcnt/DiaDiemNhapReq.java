package com.tcdt.qlnvhang.request.object.khlcnt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiaDiemNhapReq {
	private Long id;
	private Long goiThauId;
	private String maDonVi;
	private Double soLuongNhap;
	private Integer stt;
}
