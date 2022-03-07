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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	Date ngayQd;
	Long idPaHdr;
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
	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;

	@Temporal(TemporalType.DATE)
	Date tgianTbao;
	@Temporal(TemporalType.DATE)
	Date tgianDthau;
	@Temporal(TemporalType.DATE)
	Date tgianNhang;
	@Temporal(TemporalType.DATE)
	Date tgianMthau;

	Date ngayTao;
	String nguoiTao;
	String veViec;
	String namKhoach;
	String blanhDthau;

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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhQdKhlcntDtl> children1 = new ArrayList<>();

	public void setChildren1(List<HhQdKhlcntDtl> children1) {
		this.children1.clear();
		for (HhQdKhlcntDtl child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(HhQdKhlcntDtl child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}
}
