package com.tcdt.qlnvhang.request.object.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DTGoiThauDiaDiemNhapVTReq {
	private Long id;
	private Long dtvtGoiThauId;
	private Integer stt;
	private String maDonVi;
	private Long donViId;
	private BigDecimal soLuongNhap;
}
