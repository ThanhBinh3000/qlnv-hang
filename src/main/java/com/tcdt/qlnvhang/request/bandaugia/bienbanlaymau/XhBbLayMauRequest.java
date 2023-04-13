package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XhBbLayMauRequest extends BaseRequest {

	private Long id;

	private String loaiBienBan;

	private Integer nam;

	private String maDvi;

	private String maQhns;

	private Long idQd;

	private String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayQd;

	private String soHd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayHd;

	private Long idKtv;

	private String soBienBan;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMau;

	private String dviKnghiem;

	private String ddiemLayMau;

	private String loaiVthh;

	private String cloaiVthh;

	private String moTaHangHoa;

	private Long idDdiemXh;

	private String maDiemKho;

	private String maNhaKho;

	private String maNganKho;

	private String maLoKho;

	private BigDecimal soLuongLayMau;

	private String ppLayMau;

	private String chiTieuKiemTra;

	private Integer ketQuaNiemPhong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMauTu;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMauDen;

	private String maDviCuc;

	@Transient
	private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

	@Transient
	private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

	@Transient
	private List<FileDinhKemReq> fileNiemPhong = new ArrayList<>();

	private List<XhBbLayMauCtRequest> children = new ArrayList<>();
}
