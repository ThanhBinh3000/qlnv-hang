package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_DAUGIA_HANG_HDR")
@Data
public class QlnvTtinDauGiaHangHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DAUGIA_HANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DAUGIA_HANG_HDR_SEQ", allocationSize = 1, name = "QLNV_DAUGIA_HANG_HDR_SEQ")
	private Long id;
	
	Integer lanDaugia;
	Date ngayDaugia;
	String soQdKhoach;
	String maHanghoa;
	String tenDaidien;
	BigDecimal kinhPhi;	
	String ghiChu;
	String dviTochuc;
	String noiTochuc;
	String pthucDaugia;
	String maDvi;	
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
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvTtinDauGiaHangDtl> children = new ArrayList<>();

	public void setChildren(List<QlnvTtinDauGiaHangDtl> children) {
		this.children.clear();
		for (QlnvTtinDauGiaHangDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(QlnvTtinDauGiaHangDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

}
