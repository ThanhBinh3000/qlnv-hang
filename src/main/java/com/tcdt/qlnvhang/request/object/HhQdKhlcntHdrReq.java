package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdKhlcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	String cloaiVthh;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String hthucLcnt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String pthucLcnt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiHdong;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String nguonVon;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	Integer tgianThien;

	//	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Về việc không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String veViec;

//	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	Integer namKhoach;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Căn cứ quyết định giao chỉ tiêu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20-QD/TCDT")
	String soQdCc;

	@ApiModelProperty(notes = "Id của phương án đề xuất kế hoạch lựa chọn nhà thầu (nếu có)")
	private Long idPaHdr;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20-QD/TCDT")
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

//	@Size(max = 200, message = "Bảo lãnh dự thầu không được vượt quá 200 ký tự")
	@ApiModelProperty(example = "Số tiền bảo lãnh dự thầu")
	String blanhDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	String trichYeu;

	String soTrHdr;

	String phanLoai;

	Boolean lastest = false;

//	@NotNull(message = "Không được để trống")
	private Long idThHdr;
	private Long idTrHdr;

	private BigDecimal donGiaVat;

	String dienGiai;

	String ykienThamGia;

	Integer gtriDthau;

	Integer gtriHdong;

	String maDvi;
	String noiDungQd;
	String tenDuAn;
	String dienGiaiTongMucDt;
	String quyMo;
	BigDecimal tongMucDtDx;
	BigDecimal tongMucDt;
	Integer tgianThienHd;
	Integer vat;

	// Lương thực
	private List<HhQdKhlcntDtlReq> children = new ArrayList<>();

	private List<FileDinhKemReq> fileDinhKems;
	private List<FileDinhKemReq> listCcPhapLy;
	// Vật tư
	private List<HhQdKhlcntDsgthauReq> dsGoiThau;
	private ReportTemplateRequest reportTemplateRequest;
}
