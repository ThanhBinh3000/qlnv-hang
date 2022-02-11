package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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
@Table(name = "QLNV_KQ_CHUYEN_HANG")
@Data
public class QlnvKqDChuyenHang implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KQ_CHUYEN_HANG_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KQ_CHUYEN_HANG_SEQ", allocationSize = 1, name = "QLNV_KQ_CHUYEN_HANG_SEQ")
	private Long id;
	Date ngayBcao;
	String soQdinh;
	Date ngayQdinh;
	String maDviDi;
	String maDviDen;
	String soQdinhNhap;
	String soQdinhXuat;
	String soPhieuNhap;
	String soPhieuXuat;
	Date tuNgayThien;
	Date denNgayThien;	
	String maHhoa;
	String tenHhoa;
	String dviTinh;
	BigDecimal soLuongDi;
	BigDecimal soLuongDen;
	BigDecimal soChenhLech;
	String lyDo;
	String ketQua;
	String hthucDchuyen;
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
}
