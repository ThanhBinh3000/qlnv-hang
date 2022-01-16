package com.tcdt.qlnvhang.table;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "QLNV_QD_TLTH_DTL")
@Data
public class QlnvQdTlthDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_TLTH_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_TLTH_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_TLTH_DTL_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	BigDecimal soLuongDxuat;
	BigDecimal soLuongDuyet;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	private QlnvQdTlthHdr parent;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_dtl")
	private List<QlnvQdTlthDtlCtiet> children = new ArrayList<>();
	
	public void setChildren(List<QlnvQdTlthDtlCtiet> children) {
		this.children.clear();
		for (QlnvQdTlthDtlCtiet child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdTlthDtlCtiet child) {
		child.setParent(this);
		this.children.add(child);
	}
}
