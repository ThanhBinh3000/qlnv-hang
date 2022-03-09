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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = HhDthauKquaLcnt.TABLE_NAME)
@Data
public class HhDthauKquaLcnt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_KQUA_LCNT";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauKquaLcnt.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauKquaLcnt.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauKquaLcnt.TABLE_NAME + "_SEQ")
	private Long id;

	String ten;
	BigDecimal gtriTthau;
	Date tgianThHienHd;
	Date tgianNhapHang;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_gt_hdr")
	@JsonBackReference
	private HhDthauGthau parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDthauKquaLcnt.TABLE_NAME + "'")
	private List<FileDKemJoinKquaLcnt> children = new ArrayList<>();

	public void setChildren(List<FileDKemJoinKquaLcnt> children) {
		this.children.clear();
		for (FileDKemJoinKquaLcnt child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(FileDKemJoinKquaLcnt child) {
		child.setParent(this);
		this.children.add(child);
	}

}
