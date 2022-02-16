package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdXuatKhacDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idDtl;
	
	String dvts;
	
	@ApiModelProperty(example = "Tan")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHN")
	String maDvi;
	
	@NotNull(message = "Không được để trống")	
	Integer sluongDxuat;
	
	@NotNull(message = "Không được để trống")
	Integer sluongDuyet;
}
