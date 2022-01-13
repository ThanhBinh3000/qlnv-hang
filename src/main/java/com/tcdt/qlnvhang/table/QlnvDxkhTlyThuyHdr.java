package com.tcdt.qlnvhang.table;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "QLNV_DXKH_TLTH_HDR")
@Data
public class QlnvDxkhTlyThuyHdr implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DXKH_TLTH_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DXKH_TLTH_HDR_SEQ", allocationSize = 1, name = "QLNV_DXKH_TLTH_HDR_SEQ")
	private Long id;
	String soDxuat;
	@Temporal(TemporalType.DATE)
	Date ngayDxuat;
	String maDvi;
	String maHhoa;
	String tenHhoa;
	String lhinhXuat;
	@Temporal(TemporalType.DATE)
	Date tuNgayThien;
	@Temporal(TemporalType.DATE)
	Date denNgayThien;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayCcucPduyet;
	String nguoiCcucPduyet;
	Date ngayCucPduyet;
	String nguoiCucPduyet;
	Date ngayTcucPduyet;
	String nguoiTcucPduyet;
	String trangThai;
}
