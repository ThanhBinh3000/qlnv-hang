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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "HH_QD_KHLCNT_DTL")
@Data
public class HhQdKhlcntDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_DTL_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_DTL_SEQ")
	private Long id;

	String maDvi;
	String tenDvi;
	String soDxuat;
	@Temporal(TemporalType.DATE)
	Date ngayDxuat;
	String tenDuAn;
	BigDecimal soLuong;
	BigDecimal donGia;
	BigDecimal tongTien;
	Long soGthau;
	String namKhoach;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhQdKhlcntHdr parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhQdKhlcntDsgthau> children = new ArrayList<>();

	public void setChildren(List<HhQdKhlcntDsgthau> children) {
		this.children.clear();
		for (HhQdKhlcntDsgthau child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhQdKhlcntDsgthau child) {
		child.setParent(this);
		this.children.add(child);
	}

}
