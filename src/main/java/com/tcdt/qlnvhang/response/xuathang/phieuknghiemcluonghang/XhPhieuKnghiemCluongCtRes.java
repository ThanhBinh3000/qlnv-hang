package com.tcdt.qlnvhang.response.xuathang.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class XhPhieuKnghiemCluongCtRes {
	private Long id;
	private Long phieuKnghiemId;
	private Integer stt;
	private String tenCtieu;
	private String donVi;
	private String kquaKtra;
	private String pphapXdinh;
	private String chiSoChatLuong;

	public XhPhieuKnghiemCluongCtRes(XhPhieuKnghiemCluongCt item) {
		BeanUtils.copyProperties(item, this);
	}
}
