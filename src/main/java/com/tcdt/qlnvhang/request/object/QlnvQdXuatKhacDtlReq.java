package com.tcdt.qlnvhang.request.object;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdXuatKhacDtlReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SDX123")
	String soDxuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHN")
	String maDvi;
	
	@NotNull(message = "Không được để trống")	
	Integer sluongDxuat;
	
	@NotNull(message = "Không được để trống")
	Integer sluongDuyet;
	
	private List<QlnvQdXuatKhacDtlCtietReq> detail;
}
