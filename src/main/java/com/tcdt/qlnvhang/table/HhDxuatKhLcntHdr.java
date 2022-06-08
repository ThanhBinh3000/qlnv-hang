package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;

import lombok.Data;

@Entity
@Table(name = HhDxuatKhLcntHdr.TABLE_NAME)
@Data
@NamedEntityGraph(name = "HH_DX_KHLCNT_HDR.children2", attributeNodes = @NamedAttributeNode("children2"), 
subgraphs = {@NamedSubgraph(name = "HH_DX_KHLCNT_HDR.children2.children", attributeNodes = { @NamedAttributeNode("children") })})
public class HhDxuatKhLcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DX_KHLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_HDR_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_HDR_SEQ")
	private Long id;
	String soDxuat;
	String loaiVthh;
	@Transient
	String tenVthh;
	String soQd;
	String trichYeu;
	String maDvi;
	@Transient
	String tenDvi;
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
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	Long namKhoach;
	String ghiChu;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String maVtu;
	@Transient
	String tenVtu;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDxuatKhLcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinDxKhLcntHdr> children = new ArrayList<>();

	public void setChildren(List<FileDKemJoinDxKhLcntHdr> children) {
		this.children.clear();
		for (FileDKemJoinDxKhLcntHdr child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(FileDKemJoinDxKhLcntHdr child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_hdr", referencedColumnName = "id")
	private HhDxuatKhLcntLtDtl children1;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_KH_HDR")
	@JsonManagedReference
	private List<HhDxuatKhLcntDsgtDtl> children2 = new ArrayList<>();

	public void setChildren2(List<HhDxuatKhLcntDsgtDtl> children2) {
		this.children2.clear();
		for (HhDxuatKhLcntDsgtDtl child : children2) {
			child.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(HhDxuatKhLcntDsgtDtl child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhDxuatKhLcntCcxdgDtl> children3 = new ArrayList<>();

	public void setChildren3(List<HhDxuatKhLcntCcxdgDtl> children3) {
		this.children3.clear();
		for (HhDxuatKhLcntCcxdgDtl child : children3) {
			child.setParent(this);
		}
		this.children3.addAll(children3);
	}

	public void addChild3(HhDxuatKhLcntCcxdgDtl child3) {
		child3.setParent(this);
		this.children3.add(child3);
	}

}
