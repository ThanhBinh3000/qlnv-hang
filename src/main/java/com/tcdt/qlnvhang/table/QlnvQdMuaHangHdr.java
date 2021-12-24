package com.tcdt.qlnvhang.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.FilterJoinTable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_MUA_HANG_HDR")
@Data
@NamedEntityGraph(name = "QLNV_QD_MUA_HANG_HDR.children", attributeNodes = @NamedAttributeNode("children"), 
subgraphs = {@NamedSubgraph(name = "QLNV_QD_MUA_HANG_HDR.children.children", attributeNodes = { @NamedAttributeNode("children.children") })})
public class QlnvQdMuaHangHdr {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUA_HANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUA_HANG_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_MUA_HANG_HDR_SEQ")
	private Long id;

	String soQdinh;
	Date ngayQdinh;
	String soQdKhoach;
	String maHanghoa;
	String soQdinhGoc;
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
	Date tuNgayThien;
	Date denNgayThien;
	String loaiDchinh;
	Date ngayQdGoc;
	String pthucBan;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "QLNV_QD_MUA_HANG_DTL", joinColumns = @JoinColumn(name = "id_hdr"), inverseJoinColumns = @JoinColumn(name = "id"))
	@FilterJoinTable(name = "pFilter", condition = ":maDvi = ma_dvi")
	@JsonManagedReference
	private List<QlnvQdMuaHangDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvQdMuaHangDtl> children) {
		this.children.clear();
		for (QlnvQdMuaHangDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdMuaHangDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
