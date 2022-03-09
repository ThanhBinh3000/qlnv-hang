package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = HhDthauGthau2.TABLE_NAME)
@Data
public class HhDthauGthau2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_GTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauGthau2.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauGthau2.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauGthau2.TABLE_NAME + "_SEQ")
	private Long id;

	String tenGthau;
	String maHhoa;
	String dviTinh;
	BigDecimal soLuong;
	BigDecimal giaGthau;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	Date tuTgianLcnt;
	Date denTgianLcnt;
	String hthucHdong;
	Date tgianThHienHd;
	String ghiChu;
	Date tgianMoHsdxtc;
	String soQd;
	Date ngayKy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dt_hdr")
	@JsonBackReference
	private HhDthau2 parent;
}
