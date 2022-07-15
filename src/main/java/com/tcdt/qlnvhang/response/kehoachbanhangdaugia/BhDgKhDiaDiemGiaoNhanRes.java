package com.tcdt.qlnvhang.response.kehoachbanhangdaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhDgKhDiaDiemGiaoNhanRes {
	private Long id;
	private Long bhDgKehoachId;
	private Long stt;
	private String tenChiCuc;
	private String diaChi;
	private BigDecimal soLuong;
}
