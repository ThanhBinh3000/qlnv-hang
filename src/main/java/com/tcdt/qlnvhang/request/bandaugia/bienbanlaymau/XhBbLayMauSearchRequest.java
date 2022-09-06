package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XhBbLayMauSearchRequest extends BaseRequest {
	private String soBienBan;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMauTuNgay;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMauDenNgay;
	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;
}
