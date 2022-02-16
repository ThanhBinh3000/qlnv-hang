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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_TLTH_HDR")
@Data
public class QlnvQdTlthHdr implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_TLTH_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_TLTH_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_TLTH_HDR_SEQ")
	private Long id;
	String soQdinh;
	@Temporal(TemporalType.DATE)
	Date ngayQdinh;
	String maHhoa;
	String trangThai;
	@Temporal(TemporalType.DATE)
	Date ngayTao;
	String nguoiTao;
	@Temporal(TemporalType.DATE)
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	@Temporal(TemporalType.DATE)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@Temporal(TemporalType.DATE)
	Date ngayPduyet;
	String nguoiPduyet;
	@Temporal(TemporalType.DATE)
	Date tuNgayThien;
	@Temporal(TemporalType.DATE)
	Date denNgayThien;
	String lhinhXuat;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	private List<QlnvQdTlthDtl> children = new ArrayList<>();
	
	public void setChildren(List<QlnvQdTlthDtl> children) {
		this.children.clear();
		for (QlnvQdTlthDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdTlthDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
