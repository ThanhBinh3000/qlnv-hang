package com.tcdt.qlnvhang.request.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongFilterRequestDto extends BaseRequest {

	private String soPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTraTuNgay;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private String ngayLapPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTraDenNgay;

	private String maNganKho;

	private String maHangHoa;

	private String tenNguoiGiao;

	private String ketLuan;

	private String soQd;

	private String loaiVthh;
}
