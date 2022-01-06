package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class QlnvQdLcntAggReq {
	@NotNull(message = "Không được để trống")
	String loaiHanghoa;
	@NotNull(message = "Không được để trống")
	String nguonvon;
	@NotNull(message = "Không được để trống")
	String hanghoa;
	@NotNull(message = "Không được để trống")
	String soQdGiaoCtkh;
	
}
