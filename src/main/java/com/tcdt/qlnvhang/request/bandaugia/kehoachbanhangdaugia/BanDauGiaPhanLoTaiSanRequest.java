package com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BanDauGiaPhanLoTaiSanRequest {
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
