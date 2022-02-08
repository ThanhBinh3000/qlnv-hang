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
@Table(name = "QLNV_BC_KQ_TLTH")
@Data
public class QlnvBcKqTlth implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BC_KQ_TLTH_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BC_KQ_TLTH_SEQ", allocationSize = 1, name = "QLNV_BC_KQ_TLTH_SEQ")
	private Long id;
	
	String soQdTlth;
	
	Date ngayQdTlth;
	
	Date ngayTao;
	
	String nguoiTao;
	
	String maDvi;
	
	String lhinhXuat;
	
	String maHanghoa;
	
	String soQdXhang;
	
	String soPhieuXhang;
	
	BigDecimal kinhPhi;
	
	BigDecimal tienThu;
	
	Date ngayThienTu;
	
	Date ngayThienDen;
	
	String ghiChu;
	
	String trangThaiXuat;
	
	String trangThai;
	
	Date ngaySua;
	
	String nguoiSua;
	
	String ldoTuchoi;
	
	Date ngayGuiDuyet;
	
	String nguoiGuiDuyet;
	
	Date ngayPduyet;
	
	String nguoiPduyet;
	
	String tphongPduyet;
	
	Date ngayTphongPduyet;
	
	String lanhdaoPduyet;
	
	Date ngayLanhdaoPduyet;
}
