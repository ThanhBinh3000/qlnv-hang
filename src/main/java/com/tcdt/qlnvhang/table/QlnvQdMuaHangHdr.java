package com.tcdt.qlnvhang.table;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_MUA_HANG_HDR")
@Data
public class QlnvQdMuaHangHdr {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUA_HANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUA_HANG_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_MUA_HANG_HDR_SEQ")
	private Long id;

	String soQdinh;
	Date ngayQdinh;
	String soQdKhoach;
	String maHanghoa;
	String soQdinhGoc;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	Date tuNgayThien;
	Date denNgayThien;
	String loaiDchinh;
	Date ngayQdGoc;
	String pthucBan;

}
