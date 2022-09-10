package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XhBbLayMauRequest {
	private Long id;
	private Long qdgnvxId;
	private String soBienBan;
	private String donViKiemNghiem;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;
	private String maVatTuCha;
	private String maVatTu;
	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;
	private BigDecimal soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;
	private String trangThai;
	private String lyDoTuChoi;
	private String maDvi;
	private String capDvi;
	private Long nguoiGuiDuyetId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayPduyet;
	private String ketQuaNiemPhong;
	private Integer so;
	private Integer nam;
	private String loaiVthh;
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
	private List<XhBbLayMauCtRequest> chiTietList;
	private Long hopDongId;
}
