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

import lombok.Data;

@Entity
@Table(name = "QLNV_DXKH_MUA_HANG_HDR")
@Data
public class QlnvDxkhMuaHangHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DXKH_MUA_HANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DXKH_MUA_HANG_HDR_SEQ", allocationSize = 1, name = "QLNV_DXKH_MUA_HANG_HDR_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	String soQdKhoach;
	Date ngayLap;
	String maHhoa;
	String tenHhoa;
	Date tuNgayThien;
	Date denNgayThien;
	String diaDiem;
	String pthucBan;
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
	private List<QlnvDxkhMuaHangDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvDxkhMuaHangDtl> children) {
		this.children.clear();
		for (QlnvDxkhMuaHangDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvDxkhMuaHangDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
