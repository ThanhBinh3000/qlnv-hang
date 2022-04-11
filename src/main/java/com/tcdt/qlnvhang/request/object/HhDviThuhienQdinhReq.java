package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDviThuhienQdinhReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	String maDvi;

	String maDviCha;

	Double soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Tên gói thầu")
	String maVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Tên thủ kho không được vượt quá 20 ký tự")
	String thuKho;

}
