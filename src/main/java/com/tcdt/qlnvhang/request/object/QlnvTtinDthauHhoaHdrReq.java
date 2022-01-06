package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauHhoaHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	Long id;
	String maDvi;
	String soQdKh;
	String ngayQdKh;
	String maHhoa;
	String tenHhoa;
	String dviTinh;
	String nguonVon;
	String soQdLquan;
	String moTa;
	
	private List<QlnvTtinDthauHhoaDtlReq> dtlList;
	
	
	
	
}
