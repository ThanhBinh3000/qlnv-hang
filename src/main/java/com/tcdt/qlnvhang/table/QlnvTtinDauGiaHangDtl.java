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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Entity
@Table(name = "QLNV_DAUGIA_HANG_DTL")
@Data
public class QlnvTtinDauGiaHangDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DAUGIA_HANG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DAUGIA_HANG_DTL_SEQ", allocationSize = 1, name = "QLNV_DAUGIA_HANG_DTL_SEQ")
	private Long id;

	String dvts;
	String maDvi;
	@Transient
	String tenDvi;
	BigDecimal soLuong;
	BigDecimal giaKhoidiem;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_dtl")
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonIgnoreProperties("children")
	private List<QlnvTtinDauGiaHangDtlCtiet> children = new ArrayList<>();

	public void setChildren(List<QlnvTtinDauGiaHangDtlCtiet> children) {
		this.children.clear();
		for (QlnvTtinDauGiaHangDtlCtiet child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvTtinDauGiaHangDtlCtiet child) {
		child.setParent(this);
		this.children.add(child);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private QlnvTtinDauGiaHangHdr parent;

}
