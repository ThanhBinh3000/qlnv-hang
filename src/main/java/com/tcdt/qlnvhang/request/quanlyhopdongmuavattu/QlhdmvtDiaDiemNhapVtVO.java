package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class QlhdmvtDiaDiemNhapVtVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID can not null")
	private String ID;

	private String STT;

	private String donViId;

	private String maDonVi;

	private String soLuongNhap;

	private String qlhdmvtDsGoiThauId;

}
