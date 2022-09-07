package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;

import lombok.Data;

@Entity
@Table(name = HhQdKhlcntHdr.TABLE_NAME)
@Data
public class HhQdKhlcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_KHLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_HDR_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_HDR_SEQ")
	private Long id;

	String soQd;

	@Temporal(TemporalType.DATE)
	Date ngayQd;

	Long idThHdr;

	Long idTrHdr;

	String trangThai;

	Date ngaySua;

	String nguoiSua;

	String ldoTuchoi;

	Date ngayGuiDuyet;

	String nguoiGuiDuyet;

	Date ngayPduyet;

	String nguoiPduyet;

	String ghiChu;

	String loaiVthh;

	String cloaiVthh;

	String hthucLcnt;

	String pthucLcnt;

	String loaiHdong;

	String nguonVon;

	@Transient
	String tenVthh;

	@Transient
	String tenCloaiVthh;

	@Transient
	String tenHthucLcnt;

	@Transient
	String tenPthucLcnt;

	@Transient
	String tenLoaiHdong;

	@Transient
	String tenNguonVon;

	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;

	@Temporal(TemporalType.DATE)
	Date tgianDthau;

	@Temporal(TemporalType.DATE)
	Date tgianNhang;

	@Temporal(TemporalType.DATE)
	Date tgianMthau;

	@Temporal(TemporalType.DATE)
	Date ngayTao;

	String nguoiTao;

	String trichYeu;

	Long namKhoach;

	Integer tgianThienHd;

	@Temporal(TemporalType.DATE)
	Date ngayHluc;

	String maTrHdr;

	Boolean lastest;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdKhlcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinQdKhlcntHdr> children = new ArrayList<>();

	public void setChildren(List<FileDKemJoinQdKhlcntHdr> children) {
		this.children.clear();
		for (FileDKemJoinQdKhlcntHdr child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(FileDKemJoinQdKhlcntHdr child) {
		child.setParent(this);
		this.children.add(child);
	}

	@Transient
	private List<HhQdKhlcntDtl> hhQdKhlcntDtlList = new ArrayList<>();
	@Transient
	BigDecimal tongTien;
	@Transient
	Long soGthau;
	@Transient
	String tenTrangThai;
}
