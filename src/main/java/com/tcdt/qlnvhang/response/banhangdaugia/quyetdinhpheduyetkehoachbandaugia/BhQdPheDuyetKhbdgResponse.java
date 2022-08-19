package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.CommonResponse;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKhbdgResponse extends CommonResponse {
	private Long id;
	private String maDonVi;
	private String capDonVi;
	private Long namKeHoach;
	private String soQuyetDinh;
	private LocalDate ngayKy;
	private LocalDate ngayHieuLuc;
	private Long tongHopDeXuatKhbdgId;
	private String maTongHopDeXuatkhbdg;
	private LocalDate thoiHanTcBdg;
	private String trichYeu;
	private String trangThai;
	private Long nguoiGuiDuyetId;
	private Long nguoiPheDuyetId;
	private String loaiVthh;
	private String maVatTuCha;
	private String maVatTu;
	private String lyDoTuChoi;
	private List<BhQdPheDuyetKhbdgCtResponse> chiTietList;
}
