package com.tcdt.qlnvhang.response.kehoachluachonnhathau;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiaDiemNhapRes {
	private Long id;
	private Long goiThauId;
	private String maDonVi;
	private String tenDonVi;
	private Double soLuongNhap;
	private Integer stt;
}
