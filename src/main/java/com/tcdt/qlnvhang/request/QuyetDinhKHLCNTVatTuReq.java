package com.tcdt.qlnvhang.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuyetDinhKHLCNTVatTuReq {
	@ApiModelProperty(notes = "Bắt buộc nhập đối với update")
	private Long id;
	private String soQuyetDinh;
	private String veViec;
	private String ccPhapLy;
	private String diaDiemQuyMoDa;
	private String ghiChu;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayBanHanh;
	private String loaiQuyetDinh;

	List<QuyetDinhKHLCNTGoiThauVatTuReq> goiThau;
}
