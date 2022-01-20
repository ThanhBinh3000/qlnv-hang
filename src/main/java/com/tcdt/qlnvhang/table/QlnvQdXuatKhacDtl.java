package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_XUATKHAC_DTL")
@Data
public class QlnvQdXuatKhacDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_XUATKHAC_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_XUATKHAC_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_XUATKHAC_DTL_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	Integer sluongDxuat;
	Integer sluongDuyet;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QlnvQdXuatKhacDtlCtiet> children = new ArrayList<>();

	public void setChildren(List<QlnvQdXuatKhacDtlCtiet> children) {
		this.children.clear();
		for (QlnvQdXuatKhacDtlCtiet child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdXuatKhacDtlCtiet child) {
		child.setParent(this);
		this.children.add(child);
	}

    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	private QlnvQdXuatKhacHdr parent;
}
