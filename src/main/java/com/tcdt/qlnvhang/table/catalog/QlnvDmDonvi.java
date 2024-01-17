package com.tcdt.qlnvhang.table.catalog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "DM_DONVI")
@Data
public class QlnvDmDonvi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DM_COMMON_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DM_COMMON_SEQ", allocationSize = 1, name = "QLNV_DM_COMMON_SEQ")
	private Long id;
	String maDvi;
	//String maDviCha;
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
	String maQd;
	String maTr;
	String maKhqlh;
	String maKtbq;
	String maTckt;
	String maQhns;
	String maPbb;
	String tenVietTat;
	String vungMien;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "maDviCha", referencedColumnName = "maDvi")
	private QlnvDmDonvi parent;

}
