package com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QdKqlcntGtDdnVtReq {
	private Long id;
	private Long goiThauId;
	private Long donViId;
	private String maDonVi;
	private Double soLuongNhap;
	private Integer stt;
}
