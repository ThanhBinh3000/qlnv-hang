package com.tcdt.qlnvhang.table;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_DXKH_XUATKHAC_HDR")
@Data
@NamedEntityGraph(name = "QLNV_DXKH_XUATKHAC_HDR.children", attributeNodes = @NamedAttributeNode("children"))
public class QlnvDxkhXuatKhacHdr {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DXKH_XUATKHAC_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DXKH_XUATKHAC_HDR_SEQ", allocationSize = 1, name = "QLNV_DXKH_XUATKHAC_HDR_SEQ")
	String soDxuat;
	String maDvi;
	String soQdKhoach;
	Date ngayDxuat;
	String maHhoa;
	String tenHhoa;
	Date tuNgayThien;
	Date denNgayThien;
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
	String lhinhXuat;
	String trangThai;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	private List<QlnvDxkhXuatKhacDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvDxkhXuatKhacDtl> children) {
		this.children.clear();
		for (QlnvDxkhXuatKhacDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvDxkhXuatKhacDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
