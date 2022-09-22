package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhQdPheDuyetKhbdgRequest {
	private Long id;
	private String maDonVi;
	private String capDonVi;
	private Long namKeHoach;
	private String soQuyetDinh;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayKy;
	private LocalDate ngayHieuLuc;
	private Long tongHopDeXuatKhbdgId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate thoiHanTcBdg;
	private String trichYeu;
	private String trangThai;
	private Long nguoiGuiDuyetId;
	private Long nguoiPheDuyetId;
	private String loaiVthh;
	private String cloaiVthh;
	private String maVatTuCha;
	private String maVatTu;
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
	@Valid
	List<BhQdPheDuyetKhbdgCtRequest> chiTietList = new ArrayList<>();
	private String lyDoTuChoi;

}
