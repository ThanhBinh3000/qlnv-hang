package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKhbdgSearchRequest extends BaseRequest {
	private Integer namKeHoach;
	private String soQuyetDinh;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKyTuNgay;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKyDenNgay;
	private String trichYeu;
	private String maVatTuCha;
	private String maDonVi;
	private String maDonViCuc;
}
