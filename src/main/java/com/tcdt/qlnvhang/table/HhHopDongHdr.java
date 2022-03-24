package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;

import lombok.Data;

@Entity
@Table(name = HhHopDongHdr.TABLE_NAME)
@Data
public class HhHopDongHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_HOP_DONG_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhHopDongHdr.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhHopDongHdr.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhHopDongHdr.TABLE_NAME + "_SEQ")
	private Long id;

	String soHd;
	String tenHd;
	String canCu;
//	String dviTrungThau;

	@Temporal(TemporalType.DATE)
	Date tuNgayHluc;

	@Temporal(TemporalType.DATE)
	Date denNgayHluc;

	Double soNgayThien;

	@Temporal(TemporalType.DATE)
	Date tuNgayTdo;

	@Temporal(TemporalType.DATE)
	Date denNgayTdo;

	Double soNgayTdo;
	String nuocSxuat;
	String tieuChuanCl;
	Double soLuong;
	Double gtriHdTrcVat;
	Double vat;
	Double gtriHdSauVat;
	String loaiVthh;
	String loaiHd;

	@Temporal(TemporalType.DATE)
	Date ngayKy;

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
	String ghiChu;
	String namKhoach;
	String maDvi;
	@Transient
	String tenDvi;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhHopDongDtl> children = new ArrayList<>();

	public void setChildren(List<HhHopDongDtl> children) {
		this.children.clear();
		for (HhHopDongDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhHopDongDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference

	private List<HhDviLquan> children1 = new ArrayList<>();

	public void setChildren1(List<HhDviLquan> children1) {
		this.children1.clear();
		for (HhDviLquan child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(HhDviLquan child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhHopDongHdr.TABLE_NAME + "'")
	private List<FileDKemJoinHopDong> children2 = new ArrayList<>();

	public void setChildren2(List<FileDKemJoinHopDong> children2) {
		this.children2.clear();
		for (FileDKemJoinHopDong child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

}
