package com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg;
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
public class BhTongHopDeXuatKhbdgCtRequest {
	private Long id;
	private Long stt;
	private String maDiemKho;
	private String tenDiemKho;
	private String maNganKho;
	private String tenNganKho;
	private String maLoKho;
	private String tenLoKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String chungLoaiHh;
	private String maDvTaiSan;
	private BigDecimal tonKho;
	private BigDecimal soLuong;
	private BigDecimal donGia;
	private String maChiCuc;
	private String tenChiCuc;
	private Long bhDgKehoachId;
	private BigDecimal giaKhoiDiem;
	private BigDecimal soTienDatTruoc;
	private String donViTinh;
	private String ghiChu;
	private String tongHopDeXuatId;
}
