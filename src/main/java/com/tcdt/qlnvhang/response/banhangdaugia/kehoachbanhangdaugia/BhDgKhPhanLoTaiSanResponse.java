package com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhDgKhPhanLoTaiSanResponse {
	private Long id;
	private Long stt;
	private String diemKho;
	private String nganKho;
	private String loKho;
	private String chungLoaiHh;
	private String maDvTaiSan;
	private BigDecimal tonKho;
	private BigDecimal soLuong;
	private BigDecimal donGia;
	private String chiCuc;
	private Long bhDgKehoachId;
	private BigDecimal giaKhoiDiem;
	private BigDecimal soTienDatTruoc;
	private String donViTinh;
}
