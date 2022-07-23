package com.tcdt.qlnvhang.request.kehoachbanhangdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhDgKehoachSearchReq extends BaseRequest {
	private Long id;
	private Integer namKeHoach;
	private String soKeHoach;
	private String trichYeu;
	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKyTuNgay;

	@JsonFormat(pattern = LocalDateTimeUtils.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKyDenNgay;
	//Thóc, Gạo, Muối, Vật tư
	private List<String> loaiVatTuHangHoa;
}
