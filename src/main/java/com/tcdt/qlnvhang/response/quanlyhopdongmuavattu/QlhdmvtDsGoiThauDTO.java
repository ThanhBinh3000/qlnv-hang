package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QlhdmvtDsGoiThauDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ID;

	private String STT;

	private String tenGoiThau;

	private String soHieuGoiThau;

	private String soLuong;

	private String thueVat;

	private String donGiaTruocThue;

	private String THUE;

	private String giaTru0cThue;

	private String giaSauThue;

	private String ghiChu;

	private LocalDate ngayTao;

	private String nguoiTaoId;

	private LocalDate ngaySua;

	private String nguoiSuaId;

}
