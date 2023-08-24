package com.tcdt.qlnvhang.request.object;

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
public class HhQdPduyetKqlcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	Integer namKhoach;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQd;

	@Size(max = 500, message = "Về việc không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String trichYeu;

	@NotNull(message = "Không được để trống")
	@Size(max = 500, message = "Căn cứ quyết định phê duyệt kế hoạch lựa chọn nhà thầu không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQdPdKhlcnt;

	Long idQdPdKhlcnt;

	Long idQdPdKhlcntDtl;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "00")
	String ghiChu;

	String loaiVthh;

	String cloaiVthh;

	private List<HhQdPduyetKqlcntDtlReq> detailList;

	private List<FileDinhKemReq> fileDinhKems;
	private List<FileDinhKemReq> listCcPhapLy;
	private ReportTemplateRequest reportTemplateRequest;
}
