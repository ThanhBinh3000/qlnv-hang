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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

@Entity
@Table(name = "QLNV_PHIEU_NXUAT_BONGANH_HDR")
@Data
@NamedEntityGraph(name = "QLNV_PHIEU_NXUAT_BONGANH_HDR.children",attributeNodes = @NamedAttributeNode("children"))
public class QlnvPhieuNXuatBoNganhHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_PHIEU_BONGANH_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_PHIEU_BONGANH_HDR_SEQ", allocationSize = 1, name = "QLNV_PHIEU_BONGANH_HDR_SEQ")
	private Long id;
	String soPhieu;
	Date ngayLap;
	String soQdinhNhapxuat;	
	String maKho;
	String maNgan;
	String maLo;
	String maHhoa;
	String tenHhoa;
	String maDvi;
	String diaChi;
	String lhinhNhapxuat;	
	String tkhoanCo;
	String tkhoanNo;
	Date ngayNhan;
	String tenNguoiNhan;
	String loaiCtu;
	String soPhieuKtcl;
	String loaiNhapxuat;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvPhieuNXuatBoNganhDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvPhieuNXuatBoNganhDtl> children) {
		this.children.clear();
		for (QlnvPhieuNXuatBoNganhDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvPhieuNXuatBoNganhDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
