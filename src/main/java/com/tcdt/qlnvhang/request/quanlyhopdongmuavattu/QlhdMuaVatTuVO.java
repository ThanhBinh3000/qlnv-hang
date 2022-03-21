package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;


@Data
public class QlhdMuaVatTuVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID can not null")
	private String ID;

	private String canCuId;

	private String dvTrungThauId;

	private String soHopDong;

	private String tenHopDong;

	@NotNull(message = "donViTrungThauId can not null")
	private String donViTrungThauId;

	private LocalDate ngayKy;

	private LocalDate ngayTao;

	private String nguoiTaoId;

	private LocalDate ngaySua;

	private String nguoiSuaId;

	private String thongTinChungId;

}
