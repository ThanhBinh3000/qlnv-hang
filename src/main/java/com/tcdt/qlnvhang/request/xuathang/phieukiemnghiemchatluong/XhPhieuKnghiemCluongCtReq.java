package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import lombok.Data;

@Data
public class XhPhieuKnghiemCluongCtReq {
	private Long id;
	private Long phieuKnghiemId;
	private Integer stt;
	private String tenCtieu;
	private String kquaKtra;
	private String pphapXdinh;
	private String chiSoChatLuong;
}
