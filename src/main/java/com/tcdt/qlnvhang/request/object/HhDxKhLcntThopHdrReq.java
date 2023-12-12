package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;

import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class HhDxKhLcntThopHdrReq extends HhDxKhLcntTChiThopReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	String maTh;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Về việc không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String noiDung;

	String ghiChu;

	String soQdCc;
	String soTtr;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date ngayTrinh;
	private List<FileDinhKemReq> fileDinhKems;

}
