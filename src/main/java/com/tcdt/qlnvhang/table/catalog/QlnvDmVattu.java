package com.tcdt.qlnvhang.table.catalog;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "DM_VATTU")
@Data
public class QlnvDmVattu {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DM_VATTU_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DM_VATTU_SEQ", allocationSize = 1, name = "QLNV_DM_VATTU_SEQ")
	private Long id;
	private String ghiChu;
	private String nguoiTao;
	private Date ngayTao;
	private String nguoiSua;
	private Date ngaySua;
	private String ten;
	private String ma;
	private String maCha;
	private String maDviTinh;
	private String loaiHang;
	private String trangThai;
	private Boolean isLoaiKhoiDm;
	private String nhomHhBaoHiem;
	private Integer cap;
}