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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_GIAO_NHAPXUAT_HDR")
@Data
@NamedEntityGraph(name = "QLNV_QD_GIAO_NHAPXUAT_HDR.children", attributeNodes = @NamedAttributeNode("children"), subgraphs = {
		@NamedSubgraph(name = "QLNV_QD_GIAO_NHAPXUAT_HDR.children.children", attributeNodes = {
				@NamedAttributeNode("children") }) })
public class QlnvQdGiaoNhapxuatHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_GIAO_NHAPXUAT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_GIAO_NHAPXUAT_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_GIAO_NHAPXUAT_HDR_SEQ")
	private Long id;

	String soHdong;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	@Temporal(TemporalType.DATE)
	Date ngayHetHluc;
	String maHhoa;
	String tenQdinh;
	String soQdinh;
	String soLuong;
	String maDvi;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String lhinhNhapxuat;
	String dviTinh;
	@Temporal(TemporalType.DATE)
	Date tuNgayThien;
	@Temporal(TemporalType.DATE)
	Date denNgayThien;
	String loaiHdong;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvQdGiaoNhapxuatDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvQdGiaoNhapxuatDtl> children) {
		this.children.clear();
		for (QlnvQdGiaoNhapxuatDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdGiaoNhapxuatDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
