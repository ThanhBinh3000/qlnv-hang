package com.tcdt.qlnvhang.table;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_KHOACH_LCNT")
@Data
public class QlnvKhoachLcnt {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KHOACH_LCNT_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KHOACH_LCNT_SEQ", allocationSize = 1, name = "QLNV_KHOACH_LCNT_SEQ")
	private Long id;

	String loaiHanghoa;
	String soQdinh;
	Date ngayQd;
	String soVban;
	Date ngayVban;
	@Lob
	String doc;
	@Lob
	byte[] contentFile;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ldoTuchoi;
	String maDvi;
}
