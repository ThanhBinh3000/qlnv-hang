package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDinhKemReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long dataId;

	@NotNull
	@Size(max = 255, message = "Tên file không được vượt quá 255 ký tự")
	String fileName;

	@NotNull
	@Size(max = 500, message = "Đường dẫn file không được vượt quá 500 ký tự")
	String fileUrl;

	@Size(max = 20, message = "Dung lượng không được vượt quá 20 ký tự")
	String fileSize;

	private String noiDung;
}
