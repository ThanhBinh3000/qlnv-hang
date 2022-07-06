package com.tcdt.qlnvhang.response.phieuknghiemcluonghang;

import lombok.Data;

@Data
public class KquaKnghiemRes {
	private Long id;
	private Long phieuKnghiemId;
	private Integer stt;
	private String tenCtieu;
	private String donVi;
	private String kquaKtra;
	private String pphapXdinh;
	private String chiSoChatLuong;
}
