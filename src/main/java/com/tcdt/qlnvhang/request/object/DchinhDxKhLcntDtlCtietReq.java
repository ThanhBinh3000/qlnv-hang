package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class DchinhDxKhLcntDtlCtietReq {
	@NotNull(message = "Không được để trống")
	Integer soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String tenDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
}
