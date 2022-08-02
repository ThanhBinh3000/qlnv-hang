package com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg;

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
public class BhTongHopDeXuatKhbdgSearchRequest extends BaseRequest {
	private Long id;
	private Integer namKeHoach;
	private String maVatTuCha;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayTongHopTuNgay;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayTongHopDenNgay;
	private String noiDungTongHop;
}
