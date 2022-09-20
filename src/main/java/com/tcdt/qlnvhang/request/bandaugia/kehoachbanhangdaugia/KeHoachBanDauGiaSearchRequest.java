package com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeHoachBanDauGiaSearchRequest extends BaseRequest {
	private Long id;
	private Integer namKeHoach;
	private String soKeHoach;
	private String trichYeu;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayKyTuNgay;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayKyDenNgay;
	//Thóc, Gạo, Muối, Vật tư
//	private List<String> loaiVatTuHangHoa;
	private String loaiVthh;
	private String trangThai;
	private String maDvi;

}
