package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QlhdMuaVatTuQueryVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ID;

	private String canCuId;

	private String dvTrungThauId;

	private String soHopDong;

	private String tenHopDong;

	private String donViTrungThauId;

	private LocalDate ngayKy;

	private LocalDate ngayTao;

	private String nguoiTaoId;

	private LocalDate ngaySua;

	private String nguoiSuaId;

	private String thongTinChungId;

}
