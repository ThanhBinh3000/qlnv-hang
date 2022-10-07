package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;

import lombok.Data;

@Entity
@Table(name = HhQdPduyetKqlcntHdr.TABLE_NAME)
@Data
public class HhQdPduyetKqlcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_PDUYET_KQLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhQdPduyetKqlcntHdr.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhQdPduyetKqlcntHdr.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhQdPduyetKqlcntHdr.TABLE_NAME + "_SEQ")
	private Long id;

	Integer namKhoach;
	String soQd;
	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	String trichYeu;
	String soQdPdKhlcnt;
	Long idQdPdKhlcnt;

	String maDvi;
	@Transient
	String tenDvi;
	String ghiChu;
	String trangThai;
	@Transient
	String TenTrangThai;

	@Temporal(TemporalType.DATE)
	Date ngayTao;
	String nguoiTao;

	@Temporal(TemporalType.DATE)
	Date ngaySua;
	String nguoiSua;

	@Temporal(TemporalType.DATE)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;

	@Temporal(TemporalType.DATE)
	Date ngayPduyet;
	String nguoiPduyet;

	@Transient
	HhQdKhlcntHdr qdKhlcnt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdPduyetKqlcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinKquaLcntHdr> children = new ArrayList<>();

	public void setChildren(List<FileDKemJoinKquaLcntHdr> children) {
		this.children.clear();
		for (FileDKemJoinKquaLcntHdr child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(FileDKemJoinKquaLcntHdr child) {
		child.setParent(this);
		this.children.add(child);
	}

}
