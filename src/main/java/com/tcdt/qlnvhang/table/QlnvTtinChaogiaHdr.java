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
@Table(name = "QLNV_TTIN_CHAOGIA_HDR")
@Data
public class QlnvTtinChaogiaHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_CHAOGIA_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_CHAOGIA_HDR_SEQ", allocationSize = 1, name = "QLNV_TTIN_CHAOGIA_HDR_SEQ")
	private Long id;

	Long lanChaoGia;
	String maDvi;
	String soQdKhoach;
	Date ngayThien;
	String maHhoa;
	String tenHhoa;
	String loaiMuaban;
	String ghiChu;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String trangThai;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvTtinChaogiaDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvTtinChaogiaDtl> children) {
		this.children.clear();
		for (QlnvTtinChaogiaDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvTtinChaogiaDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
