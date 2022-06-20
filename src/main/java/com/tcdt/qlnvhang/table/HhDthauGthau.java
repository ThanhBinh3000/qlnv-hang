package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinGoiThau;

import lombok.Data;

@Entity
@Table(name = HhDthauGthau.TABLE_NAME)
@Data
public class HhDthauGthau implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_GTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauGthau.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauGthau.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauGthau.TABLE_NAME + "_SEQ")
	private Long id;

	Long idGoiThau;
	String soQdPdKhlcnt;
	String ngayQdPdKhlcnt;
	String tenGthau;
	String loaiVthh;
	@Transient
	String tenVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String dviTinh;
	String maHhoa;
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
	String nhaThauTthao;
	Integer vat;
	Long donGia;
	Integer tgianThienHd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dt_hdr")
	@JsonBackReference
	private HhDthau parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_gt_hdr")
	@JsonManagedReference
	private List<HhDthauNthauDuthau> children = new ArrayList<>();

	public void setChildren(List<HhDthauNthauDuthau> children) {
		this.children.clear();
		for (HhDthauNthauDuthau child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhDthauNthauDuthau child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_gt_hdr")
	@JsonManagedReference
	private List<HhDthauHsoKthuat> children1 = new ArrayList<>();

	public void setChildren1(List<HhDthauHsoKthuat> children1) {
		this.children1.clear();
		for (HhDthauHsoKthuat child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(HhDthauHsoKthuat child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_gt_hdr")
	@JsonManagedReference
	private List<HhDthauHsoTchinh> children2 = new ArrayList<>();

	public void setChildren2(List<HhDthauHsoTchinh> children2) {
		this.children2.clear();
		for (HhDthauHsoTchinh child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(HhDthauHsoTchinh child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_gt_hdr")
	@JsonManagedReference
	private List<HhDthauTthaoHdong> children3 = new ArrayList<>();

	public void setChildren3(List<HhDthauTthaoHdong> children3) {
		this.children3.clear();
		for (HhDthauTthaoHdong child3 : children3) {
			child3.setParent(this);
		}
		this.children3.addAll(children3);
	}

	public void addChild3(HhDthauTthaoHdong child3) {
		child3.setParent(this);
		this.children3.add(child3);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_gt_hdr")
	@JsonManagedReference
	private List<HhDthauKquaLcnt> children4 = new ArrayList<>();

	public void setChildren4(List<HhDthauKquaLcnt> children4) {
		this.children4.clear();
		for (HhDthauKquaLcnt child4 : children4) {
			child4.setParent(this);
		}
		this.children4.addAll(children4);
	}

	public void addChild4(HhDthauKquaLcnt child4) {
		child4.setParent(this);
		this.children4.add(child4);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDthauGthau.TABLE_NAME + "'")
	private List<FileDKemJoinGoiThau> children5 = new ArrayList<>();

	public void setChildren5(List<FileDKemJoinGoiThau> children5) {
		this.children5.clear();
		for (FileDKemJoinGoiThau child5 : children5) {
			child5.setParent(this);
		}
		this.children5.addAll(children5);
	}

	public void addChild5(FileDKemJoinGoiThau child5) {
		child5.setParent(this);
		this.children5.add(child5);
	}

}
