package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

@Entity
@Table(name = "HH_DVI_THUCHIEN_QDINH")
@Data
public class HhDviThuchienQdinh implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DVI_THUCHIEN_QDINH_SEQ")
	@SequenceGenerator(sequenceName = "HH_DVI_THUCHIEN_QDINH_SEQ", allocationSize = 1, name = "HH_DVI_THUCHIEN_QDINH_SEQ")
	private Long id;

	String maDvi;
	Double soLuong;
	String maVthh;
	String thuKho;
	String ghiChu;

	@Transient
	String tenDvi;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhQdGiaoNvuNhapxuatHdr parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhDviThQdDtl> children = new ArrayList<>();

	public void setChildren(List<HhDviThQdDtl> children1) {
		this.children.clear();
		for (HhDviThQdDtl child1 : children1) {
			child1.setParent(this);
		}
		this.children.addAll(children1);
	}

	public void addChild(HhDviThQdDtl child1) {
		child1.setParent(this);
		this.children.add(child1);
	}
}
