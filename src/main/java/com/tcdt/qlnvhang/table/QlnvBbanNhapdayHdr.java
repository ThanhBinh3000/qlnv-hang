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
@Table(name = "QLNV_BBAN_NHAPDAY_HDR")
@Data
@NamedEntityGraph(name = "QLNV_BBAN_NHAPDAY_HDR.children", attributeNodes = @NamedAttributeNode("children"), subgraphs = {
		@NamedSubgraph(name = "QLNV_BBAN_NHAPDAY_HDR.children.children", attributeNodes = {
				@NamedAttributeNode("children") }) })
public class QlnvBbanNhapdayHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BBAN_NHAPDAY_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BBAN_NHAPDAY_HDR_SEQ", allocationSize = 1, name = "QLNV_BBAN_NHAPDAY_HDR_SEQ")
	private Long id;

	String maNgan;
	@Temporal(TemporalType.DATE)
	Date ngayLap;
	String soQdinh;
	String maLo;
	String maHhoa;
	String maKho;
	String soBban;
	String thuKho;
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
	@Temporal(TemporalType.DATE)
	Date ngayKthucNhap;
	String kthuatVien;
	String thuTruong;
	@Temporal(TemporalType.DATE)
	Date ngayBdauNhap;
	String keToan;
	String trangThai;

	String tkPduyet;
	Date ngayTkPduyet;
	String kttPduyet;
	Date ngayKttPduyet;
	String ktvPduyet;
	Date ngayKtvPduyet;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvBbanNhapdayDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvBbanNhapdayDtl> children) {
		this.children.clear();
		for (QlnvBbanNhapdayDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvBbanNhapdayDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
