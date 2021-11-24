package com.tcdt.qlnvhang.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class QlnvDmDonviEntity {
	@Id
	Long id;
	String maDvi;
	String maDviCha;
	String tenDvi;
	String maHchinh;
	String maTinh;
	String maQuan;
	String maPhuong;
	String diaChi;
	String capDvi;
	String kieuDvi;
	String loaiDvi;
	String ghiChu;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
}
