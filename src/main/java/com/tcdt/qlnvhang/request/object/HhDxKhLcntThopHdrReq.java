package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
	private List<FileDinhKemReq> fileDinhKems;

}
