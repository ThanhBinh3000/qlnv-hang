package com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhTongHopDeXuatKhbdgResponse {
	private Long id;
	private Integer namKeHoach;
	private String loaiHangHoa;
	private LocalDate ngayKyTuNgay;
	private LocalDate ngayKyDenNgay;
	private LocalDate ngayTongHop;
	private String noiDungTongHop;
	private LocalDate tgDuKienTcbdgTuNgay;
	private LocalDate tgDuKienTcbdgDenNgay;
	private String ghiChu;
	private String maDonVi;
	private String capDonVi;
	private String trangThai;
	private String maTongHop;
	private IdAndNameDto qdPheDuyetKhbdg;
}
