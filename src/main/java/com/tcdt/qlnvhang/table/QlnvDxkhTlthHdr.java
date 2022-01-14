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
@Table(name = "QLNV_DXKH_TLTH_HDR")
@Data
public class QlnvDxkhTlthHdr implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DXKH_TLTH_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DXKH_TLTH_HDR_SEQ", allocationSize = 1, name = "QLNV_DXKH_TLTH_HDR_SEQ")
	private Long id;
	String soDxuat;
	@Temporal(TemporalType.DATE)
	Date ngayDxuat;
	String maDvi;
	String maHhoa;
	String tenHhoa;
	String lhinhXuat;
	@Temporal(TemporalType.DATE)
	Date tuNgayThien;
	@Temporal(TemporalType.DATE)
	Date denNgayThien;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayCcucPduyet;
	String nguoiCcucPduyet;
	Date ngayCucPduyet;
	String nguoiCucPduyet;
	Date ngayTcucPduyet;
	String nguoiTcucPduyet;
	String trangThai;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	private List<QlnvDxkhTlthDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvDxkhTlthDtl> children) {
		this.children.clear();
		for (QlnvDxkhTlthDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvDxkhTlthDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
