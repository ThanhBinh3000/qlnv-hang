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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = HhDthau.TABLE_NAME)
@Data
public class HhDthau implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthau.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthau.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhDthau.TABLE_NAME
			+ "_SEQ")
	private Long id;

	String soHd;
	String tenHd;
	String soQd;
	String loaiVthh;
	String maDvi;
	String namKhoach;
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
	String ghiChu;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_dt_hdr")
	@JsonManagedReference
	private List<HhDthauGthau> children = new ArrayList<>();

	public void setChildren(List<HhDthauGthau> children) {
		this.children.clear();
		for (HhDthauGthau child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhDthauGthau child) {
		child.setParent(this);
		this.children.add(child);
	}

}
