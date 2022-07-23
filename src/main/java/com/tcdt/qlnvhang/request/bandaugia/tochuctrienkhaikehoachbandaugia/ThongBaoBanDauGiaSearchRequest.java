package com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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
public class ThongBaoBanDauGiaSearchRequest extends BaseRequest {
	private Long id;
	private Integer namKeHoach;
	private String loaiHangHoa;
	private String soQuyetDinhPheDuyetKHBDG;
	private String maThongBaoBDG;
	private String trichYeu;
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayToChucBDGTuNgay;

	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayToChucBDGDenNgay;
}
