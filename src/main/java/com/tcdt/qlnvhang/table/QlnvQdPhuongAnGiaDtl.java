package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_PAN_GIA_DTL")
@Data
public class QlnvQdPhuongAnGiaDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_PAN_GIA_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_PAN_GIA_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_PAN_GIA_DTL_SEQ")
	private Long id;

	String soDxuat;
	String maDvi;
	BigDecimal soLuongDxuat;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_dtl")
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonIgnoreProperties("children")
	private List<QlnvQdPhuongAnGiaDtlCtiet> children = new ArrayList<>();

	public void setChildren(List<QlnvQdPhuongAnGiaDtlCtiet> children) {
		this.children.clear();
		for (QlnvQdPhuongAnGiaDtlCtiet child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdPhuongAnGiaDtlCtiet child) {
		child.setParent(this);
		this.children.add(child);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private QlnvQdPhuongAnGiaHdr parent;

}
