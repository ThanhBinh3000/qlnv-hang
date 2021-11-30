package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	String soQdinh;
	Date ngayQd;
	String soQdKh;
	String loaiHanghoa;
	String maHanghoa;
	String nguonvon;
	String loaiQd;
	String soQdinhGoc;
	String ldoTuchoi;
	Date tuNgayThien;
	Date denNgayThien;
	private List<QlnvQdLcntDtlReq> detail;
}
