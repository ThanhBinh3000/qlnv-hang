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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
@Table(name = HhQdPduyetKqlcntHdr.TABLE_NAME)
@Data
@NamedEntityGraph(name = HhQdPduyetKqlcntHdr.TABLE_NAME + ".children", attributeNodes = {
		@NamedAttributeNode("children"), @NamedAttributeNode("children1") })

public class HhQdPduyetKqlcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_PDUYET_KQLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhQdPduyetKqlcntHdr.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhQdPduyetKqlcntHdr.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhQdPduyetKqlcntHdr.TABLE_NAME + "_SEQ")
	private Long id;

	String soQd;
	@Temporal(TemporalType.DATE)
	Date ngayQd;
	String loaiVthh;
	String namKhoach;
	String veViec;
	String canCu;
	String maDvi;
	String ghiChu;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdPduyetKqlcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinKquaLcntHdr> children = new ArrayList<>();

	public void setChildren(List<FileDKemJoinKquaLcntHdr> children) {
		this.children.clear();
		for (FileDKemJoinKquaLcntHdr child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(FileDKemJoinKquaLcntHdr child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhQdPduyetKqlcntDtl> children1 = new ArrayList<>();

	public void setChildren1(List<HhQdPduyetKqlcntDtl> children1) {
		this.children1.clear();
		for (HhQdPduyetKqlcntDtl child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(HhQdPduyetKqlcntDtl child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}
}
