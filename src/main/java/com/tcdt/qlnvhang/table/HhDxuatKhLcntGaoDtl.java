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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "HH_DXUAT_KH_LCNT_GAO_DTL")
@Data
public class HhDxuatKhLcntGaoDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_GAO_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_GAO_DTL_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_GAO_DTL_SEQ")
	private Long id;

	String tenDuAn;
	BigDecimal tongMucDt;
	@Temporal(TemporalType.DATE)
	Date tuNgayThHien;
	@Temporal(TemporalType.DATE)
	Date denNgayThHien;
	String tchuanCluong;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	@Temporal(TemporalType.DATE)
	Date tgianTbao;
	@Temporal(TemporalType.DATE)
	Date tgianDongThau;
	@Temporal(TemporalType.DATE)
	Date tgianMoThau;
	String loaiHdong;
	@Temporal(TemporalType.DATE)
	Date tgianThHienHd;
	@Temporal(TemporalType.DATE)
	Date tgianNhapHang;
	String blanhDthau;
	String ghiChu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhDxuatKhLcntHdr parent;

}
