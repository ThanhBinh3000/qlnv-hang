package com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class XhBbLayMauResponse extends CommonResponse {
	private Long id;
	private Long qdgnvxId;
	private String soBienBan;
	private String donViKiemNghiem;
	private String tenDonViKiemNghiem;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;
	private String maVatTuCha;
	private String maVatTu;
	private String maDiemKho;
	private String tenDiemKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String maNganKho;
	private String tenNganKho;
	private String maNganLo;
	private String tenNganLo;
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
	private List<XhBbLayMauCtResponse> chiTietList = new ArrayList<>();
	private List<FileDinhKem> fileDinhKems;
	private Long hopDongId;
}
