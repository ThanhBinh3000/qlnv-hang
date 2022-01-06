package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	String soQdinh;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayQd;
	String loaiHanghoa;
	String maHanghoa;
	String nguonvon;
	String loaiQd;
	String soQdinhGoc;
	String ldoTuchoi;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date tuNgayThien;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date denNgayThien;
	String soQdGiaoCtkh;
	String loaiDieuChinh;
	private List<QlnvQdLcntDtlReq> detail;
}
