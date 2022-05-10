package com.tcdt.qlnvhang.request.quanlyphieukiemtrachatluonghangluongthuc;

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
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlpktclhPhieuKtChatLuongFilterRequestDto extends BaseRequest {

	private Long soPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTraTuNgay;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private String ngayLapPhieu;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKiemTraDenNgay;

	private Long maNganKho;

	private Long maHangHoa;

	private String maDonVi;

	private String tenNguoiGiao;
}
