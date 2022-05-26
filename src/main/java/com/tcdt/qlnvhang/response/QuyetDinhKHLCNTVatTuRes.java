package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuyetDinhKHLCNTVatTuRes {
	private Long id;
	private String soQuyetDinh;
	private String veViec;
	private String ccPhapLy;
	private String diaDiemQuyMoDa;
	private String ghiChu;
	private LocalDate ngayBanHanh;
	private String loaiQuyetDinh;
	private List<QuyetDinhKHLCNTGoiThauVatTuRes> goiThauList;
}
