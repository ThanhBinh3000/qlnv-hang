package com.tcdt.qlnvhang.table;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_MUATT_HDR")
@Data
@FilterDef(name = "pFilter", parameters = @ParamDef(name = "maDvi", type = "string"))
public class QlnvQdMuattHdr2 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_MUATT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_MUATT_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_MUATT_HDR_SEQ")
	private Long id;

	String soQdinh;
	Date ngayQd;
	String soQdKh;
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

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "QLNV_QD_MUATT_DTL", joinColumns = @JoinColumn(name = "id_hdr"), inverseJoinColumns = @JoinColumn(name = "id"))
	@FilterJoinTable(name = "pFilter", condition = ":maDvi = ma_dvi")
	@JsonManagedReference
	private List<QlnvQdMuattDtl2> children = new ArrayList<>();

	public void setChildren(List<QlnvQdMuattDtl2> children) {
		this.children.clear();
		for (QlnvQdMuattDtl2 child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdMuattDtl2 child) {
		child.setParent(this);
		this.children.add(child);
	}

}
