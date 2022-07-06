package com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang;

import lombok.Data;

@Data
public class KquaKnghiemReq {
	private Long id;
	private Long phieuKnghiemId;
	private Integer stt;
	private String tenCtieu;
	private String kquaKtra;
	private String pphapXdinh;
	private String chiSoChatLuong;
}
