package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauVtuHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	Long id;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "MDV1")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "SQDKH123")
	@Size(max = 50, message = "Số quyết định KH không được vượt quá 50 ký tự")
	String soQdKh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdKh;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "MVT1")
	@Size(max = 50, message = "Mã vật tư không được vượt quá 50 ký tự")
	String maVtu;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "TVT1")
	@Size(max = 50, message = "Tên vật tư không được vượt quá 50 ký tự")
	String tenVtu;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "Tấn")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "nguonVon")
	@Size(max = 50, message = "Nguồn vốn không được vượt quá 50 ký tự")
	String nguonVon;
	
	@ApiModelProperty(example = "SQDLQ123")
	@Size(max = 250, message = "Số quyết định liên quan không được vượt quá 250 ký tự")
	String soQdLquan;
	
	@Size(max = 250, message = "Mô tả không được vượt quá 250 ký tự")
	String moTa;
	
	private List<QlnvTtinDthauVtuDtlReq> dtlList;
	
	
	
	
}
