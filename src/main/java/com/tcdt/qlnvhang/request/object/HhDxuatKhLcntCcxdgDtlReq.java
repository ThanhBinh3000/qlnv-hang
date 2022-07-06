package com.tcdt.qlnvhang.request.object;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDxuatKhLcntCcxdgDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên tài liệu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tailieumau")
	String tenTlieu;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại căn cứ không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "00")
	String loaiCanCu;
	
	List<FileDinhKemReq> children;
}
