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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_QD_XUATKHAC_HDR")
@Data
public class QlnvQdXuatKhacHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_XUATKHAC_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_XUATKHAC_HDR_SEQ", allocationSize = 1, name = "QLNV_QD_XUATKHAC_HDR_SEQ")
	private Long id;
	String soQdinh;
	Date ngayQdinh;
	String maHanghoa;
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
	String lhinhXuat;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QlnvQdXuatKhacDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvQdXuatKhacDtl> children) {
		this.children.clear();
		for (QlnvQdXuatKhacDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvQdXuatKhacDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
