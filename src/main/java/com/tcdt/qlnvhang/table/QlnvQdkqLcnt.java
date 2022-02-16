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
@Table(name = "QLNV_QDKQ_LCNT")
@Data
public class QlnvQdkqLcnt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QDKQ_LCNT_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QDKQ_LCNT_SEQ", allocationSize = 1, name = "QLNV_QDKQ_LCNT_SEQ")
	private Long id;
	Long idTtinDthau;
	Long idDviNopthau;
	String soQdinhKh;
	Date ngayQd;
	String soQdinh;
	String loaiHanghoa;
	BigDecimal donGiaTrcThue;
	BigDecimal vat;
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
	String dviTrungthau;
	BigDecimal giaThau;
	BigDecimal donGiaTrung;
	String ghiChu;
	String kqDthau;
	String lyDo;
	Date ngayQdKh;
}
