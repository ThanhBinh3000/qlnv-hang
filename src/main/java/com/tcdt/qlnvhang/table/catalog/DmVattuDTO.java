package com.tcdt.qlnvhang.table.catalog;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
public class DmVattuDTO {
	String ghiChu;
	String ma;
	Date ngaySua;
	Date ngayTao;
	String nguoiSua;
	String nguoiTao;
	String ten;
	String trangThai;
	String maDviTinh;
	String maCha;
	String cap;
	String chon;
	String loaiHang;
	String kyHieu;
	String dviQly;
	String nhomHhBaoHiem;
}