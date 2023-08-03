package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhDxKhLcntThopSearchReq extends BaseRequest {
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayThop;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayThop;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String cloaiVthh;

	String noiDung;

	String trangThai;

	String soQd;
	private Long id;
	private ReportTemplateRequest reportTemplateRequest;
}
