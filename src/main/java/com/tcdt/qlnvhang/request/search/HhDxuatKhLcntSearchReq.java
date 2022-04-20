package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhDxuatKhLcntSearchReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQDGCT123")
	String soDxuat;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayKy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayKy;

	@Size(max = 250, message = "Trích yếu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "SQDGCT123")
	String trichYeu;

	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	@Size(max = 2, message = "Loại vật tư hàng hóa không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String maDvi;

}
