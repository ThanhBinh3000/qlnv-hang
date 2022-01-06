package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "QLNV_BKE_CANHANG_HDR")
@Data
@NamedEntityGraph(name = "QLNV_BKE_CANHANG_HDR.children", attributeNodes = @NamedAttributeNode("children"), subgraphs = {
		@NamedSubgraph(name = "QLNV_BKE_CANHANG_HDR.children.children", attributeNodes = {
				@NamedAttributeNode("children") }) })
public class QlnvBkeCanhangHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BKE_CANHANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BKE_CANHANG_HDR_SEQ", allocationSize = 1, name = "QLNV_BKE_CANHANG_HDR_SEQ")
	private Long id;

	String maNgan;
	@Temporal(TemporalType.DATE)
	Date ngayLap;
	String soHdong;
	String maLo;
	String maHhoa;
	String maKho;
	String soBke;
	String diaChi;
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
	String soKho;
	String dviTinh;
	Date ngayGiao;
	String tenNguoiGiao;
	String loaiBke;
	String soPhieu;
	String trangThai;
	BigDecimal tluongBbi;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvBkeCanhangDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvBkeCanhangDtl> children) {
		this.children.clear();
		for (QlnvBkeCanhangDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvBkeCanhangDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
