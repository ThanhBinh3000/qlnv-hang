package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QlhdmvtTtChuDauTuQueryVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ID;

	private String tenDvDtnn;

	private String diaChi;

	private String maSoThue;

	private String soDienThoai;

	private String soTaiKhoan;

	private String tenNguoiDaiDien;

	private String chucVu;

	private LocalDate ngayTao;

	private String nguoiTaoId;

	private LocalDate ngaySua;

	private String nguoiSuaId;

}
