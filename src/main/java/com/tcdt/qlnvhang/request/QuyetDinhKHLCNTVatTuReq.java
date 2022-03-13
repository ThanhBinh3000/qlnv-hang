package com.tcdt.qlnvhang.request;

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
public class QuyetDinhKHLCNTVatTuReq {
	private Long id;
	private String soQuyetDinh;
	private String veViec;
	private String ccPhapLy;
	private String diaDiemQuyMoDa;
	private String ghiChu;
	private LocalDate ngayBanHanh;
	private String loaiQuyetDinh;

	List<QuyetDinhKHLCNTGoiThauVatTuReq> goiThau;
}
