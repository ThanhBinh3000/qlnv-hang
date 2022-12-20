package com.tcdt.qlnvhang.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class StatusReq {
	@NotNull(message = "Không được để trống")
	Long id;
	@NotNull(message = "Không được để trống")
	String trangThai;
	@Size(max = 250, message = "Lý do không được vượt quá 250 ký tự")
	String lyDo;
	String lyDoTuChoi;

}
