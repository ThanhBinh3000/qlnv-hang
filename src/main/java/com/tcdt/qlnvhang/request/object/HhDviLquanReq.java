package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDviLquanReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	String ma;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String ten;

	@Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
	String diaChi;

	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	String sdt;

	@Size(max = 50, message = "Số tài khoản không được vượt quá 50 ký tự")
	String stk;

	@Size(max = 20, message = "Mã số thuế không được vượt quá 20 ký tự")
	String mst;

	@Size(max = 200, message = "Tên người đại diện không được vượt quá 200 ký tự")
	String tenNguoiDdien;

	@Size(max = 200, message = "Chức vụ không được vượt quá 200 ký tự")
	String chucVu;

	@NotNull(message = "Không được để trống")
	@Size(max = 1, message = "Loại đơn vị không được vượt quá 1 ký tự")
	@ApiModelProperty(example = "A")
	String type;

}
