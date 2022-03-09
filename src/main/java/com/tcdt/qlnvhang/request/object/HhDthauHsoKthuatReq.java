package com.tcdt.qlnvhang.request.object;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDthauHsoKthuatReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên nhà thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên nhà thầu")
	String ten;

	Long diem;

	@Size(max = 500, message = "Lý do không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Lý do")
	String lyDo;

	@NotNull(message = "Không được để trống")
	@Size(max = 1, message = "Đáp ứng không được vượt quá 1 ký tự")
	@ApiModelProperty(example = "Y")
	String dapUng;

	Long idGtHdr;

	private List<FileDinhKemReq> fileDinhKems;
}
