package com.tcdt.qlnvhang.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;

@Data
public class StatusReq {
	@NotNull(message = "Không được để trống")
	Long id;
	@NotNull(message = "Không được để trống")
	String trangThai;
	@Size(max = 250, message = "Lý do không được vượt quá 250 ký tự")
	String lyDo;
	String lyDoTuChoi;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
}
