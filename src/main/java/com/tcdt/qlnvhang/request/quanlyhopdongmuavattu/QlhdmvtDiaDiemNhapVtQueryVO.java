package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;


import lombok.Data;

import java.io.Serializable;

@Data
public class QlhdmvtDiaDiemNhapVtQueryVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ID;

	private String STT;

	private String donViId;

	private String maDonVi;

	private String soLuongNhap;

	private String qlhdmvtDsGoiThauId;

}
