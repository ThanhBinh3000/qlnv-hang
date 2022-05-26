package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DTGoiThauDiaDiemNhapVTRes {
	private Long id;
	private Long dtvtGoiThauId;
	private Integer stt;
	private String tenDonVi;
	private String maDonVi;
	private Long donViId;
	private BigDecimal soLuongNhap;
}
