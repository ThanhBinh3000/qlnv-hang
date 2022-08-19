package com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhTongHopDeXuatKhbdgResponse  extends CommonResponse {
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
	private IdAndNameDto qdPheDuyetKhbdg;
	private String maVatTu;
	private String maVatTuCha;
	private String tenVatTuCha;
	private String loaiVthh;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String lyDoTuChoi;
	private List<BhTongHopDeXuatCtResponse> chiTietList;

}
