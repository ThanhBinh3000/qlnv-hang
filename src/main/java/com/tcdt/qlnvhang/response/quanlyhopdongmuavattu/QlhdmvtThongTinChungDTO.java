package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QlhdmvtThongTinChungDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ID;

	private LocalDate nhlTuNgay;

	private String nhlDenNgay;

	private String soNgayTh;

	private String loaiHangId;

	private LocalDate tocDoCcTuNgay;

	private LocalDate tocDoCcDenNgay;

	private String soNgayTheoTenDo;

	private String loaiHopDongId;

	private String nuocSanXuatId;

	private String tieuChuanCl;

	private String soLuong;

	private String giaTriHdTruocThue;

	private String thueVat;

	private String giaTriHdSauThu;

	private String fileDinhKemId;

	private String chuDauTuId;

	private String dvCungCapId;

	private String ghiChu;

	private LocalDate ngayTao;

	private String nguoiTaoId;

	private LocalDate ngaySua;

	private String nguoiSuaId;

}
