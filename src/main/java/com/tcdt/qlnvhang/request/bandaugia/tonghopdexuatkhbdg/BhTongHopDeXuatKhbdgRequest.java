package com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhTongHopDeXuatKhbdgRequest {
	private Long id;
	private Integer namKeHoach;
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
	private String loaiVthh;
	private String cloaiVthh;
	private List<BhTongHopDeXuatCtRequest> chiTietList = new ArrayList<>();
}
