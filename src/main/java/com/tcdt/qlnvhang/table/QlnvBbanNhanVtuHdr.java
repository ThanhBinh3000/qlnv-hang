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
@Table(name = "QLNV_BBAN_NHAN_VTU_HDR")
@Data
@NamedEntityGraph(name = "QLNV_BBAN_NHAN_VTU_HDR.children", attributeNodes = @NamedAttributeNode("children"), subgraphs = {
		@NamedSubgraph(name = "QLNV_BBAN_NHAN_VTU_HDR.children.children", attributeNodes = {
				@NamedAttributeNode("children") }) })
public class QlnvBbanNhanVtuHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BBAN_NHAN_VTU_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BBAN_NHAN_VTU_HDR_SEQ", allocationSize = 1, name = "QLNV_BBAN_NHAN_VTU_HDR_SEQ")
	private Long id;

	String ddienCuc;
	@Temporal(TemporalType.DATE)
	Date ngayLap;
	String soQdinh;
	String maDviCcuc;
	String maHhoa;
	String maDviCuc;
	String soBban;
	String dviGiao;
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
	String bbanKtraCluong;
	String ddienGiao;
	@Temporal(TemporalType.DATE)
	Date ngayGiao;
	String ddienCcuc;
	String soHsoKthuat;
	String trangThai;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvBbanNhanVtuDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvBbanNhanVtuDtl> children) {
		this.children.clear();
		for (QlnvBbanNhanVtuDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvBbanNhanVtuDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
