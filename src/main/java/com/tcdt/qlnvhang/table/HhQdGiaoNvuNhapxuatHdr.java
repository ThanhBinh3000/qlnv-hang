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
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapxuat;

import lombok.Data;

@Entity
@Table(name = HhQdGiaoNvuNhapxuatHdr.TABLE_NAME)
@Data
public class HhQdGiaoNvuNhapxuatHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "NH_QD_GIAO_NVU_NHAPXUAT";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_SEQ")
	private Long id;

	String soQd;
	String veViec;
	@Temporal(TemporalType.DATE)
	Date ngayKy;

	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	String soHd;
	String maDvi;
	String loaiQd;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String ghiChu;
	String capDvi;
	@Transient
	String tenDvi;

	@Transient
	String tenLoaiQd;

	@Transient
	String tenTrangThai;

	@Transient
	Long hdId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhQdGiaoNvuNhapxuatDtl> children = new ArrayList<>();

	public void setChildren(List<HhQdGiaoNvuNhapxuatDtl> children) {
		this.children.clear();
		for (HhQdGiaoNvuNhapxuatDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhQdGiaoNvuNhapxuatDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhDviThuchienQdinh> children1 = new ArrayList<>();*/

	/*public void setChildren1(List<HhDviThuchienQdinh> children1) {
		this.children1.clear();
		for (HhDviThuchienQdinh child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(HhDviThuchienQdinh child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}*/

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdGiaoNvuNhapxuatHdr.TABLE_NAME + "'")
	private List<FileDKemJoinQdNhapxuat> children2 = new ArrayList<>();

	public void setChildren2(List<FileDKemJoinQdNhapxuat> children2) {
		this.children2.clear();
		for (FileDKemJoinQdNhapxuat child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(FileDKemJoinQdNhapxuat child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

	Integer namNhap;

	Date ngayQdinh;
}
