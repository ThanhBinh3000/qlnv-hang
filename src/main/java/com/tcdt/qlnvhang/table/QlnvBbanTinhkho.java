package com.tcdt.qlnvhang.table;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_BBAN_TINHKHO")
@Data
public class QlnvBbanTinhkho {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BBAN_TINHKHO_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BBAN_TINHKHO_SEQ", allocationSize = 1, name = "QLNV_BBAN_TINHKHO_SEQ")
	private Long id;
	
	String soBban;
	String maDvi;
	String maKho;
	Date ngayLap;
	String maHhoa;
	String tenHhoa;
	String maLo;
	String maThukho;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String maNgan;
	String kthuatVien;
	String keToan;
	String thuTruong;
	String thuKho;
	BigDecimal sluongNhap;
	BigDecimal sluongXuat;
	BigDecimal sluongTon;
	BigDecimal sluongTonThucTe;
	BigDecimal sluongChlech;
	String nguyenNhan;
	String kienNghi;
	String trangThai;
}
