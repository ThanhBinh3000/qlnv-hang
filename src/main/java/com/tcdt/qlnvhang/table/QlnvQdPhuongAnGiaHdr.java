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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_PAN_GIA_HDR")
@Data
@NamedEntityGraph(name = "QLNV_QD_PAN_GIA_HDR.children", attributeNodes = @NamedAttributeNode(value = "children", subgraph = "QLNV_QD_PAN_GIA_HDR.children.children"), 
subgraphs = @NamedSubgraph(name = "QLNV_QD_PAN_GIA_HDR.children.children", attributeNodes = @NamedAttributeNode("children")))
public class QlnvQdPhuongAnGiaHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_PAN_GIA_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_PAN_GIA_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_PAN_GIA_HDR_SEQ")
	private Long id;
	String soQdinh;
	Date ngayQd;
	String namKhoach;
	Date tuNgayHluc;
	Date denNgayHluc;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvQdPhuongAnGiaDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvQdPhuongAnGiaDtl> children) {
		this.children.clear();
		for (QlnvQdPhuongAnGiaDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdPhuongAnGiaDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
