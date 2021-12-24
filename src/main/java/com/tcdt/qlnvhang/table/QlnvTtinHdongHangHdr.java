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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "QLNV_TTIN_HDONG_HANG_HDR")
@Data
public class QlnvTtinHdongHangHdr implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_HDONG_HANG_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_HDONG_HANG_HDR_SEQ", allocationSize = 1, name = "QLNV_TTIN_HDONG_HANG_HDR_SEQ")
	private Long id;

	String soHdong;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	@Temporal(TemporalType.DATE)
	Date ngayHetHluc;
	String maHhoa;
	String tenHdong;
	String soQdinh;
	Long idTtinDthau;
	String maDvi;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String dviThien;
	String mstDvi;
	String diaChi;
	String nguoiDaiDien;
	String soDthoai;
	BigDecimal giaTriHdong;
	BigDecimal giaNoVat;
	Float vat;
	String tthaiHdong;
	String loaiHdong;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvTtinHdongHangDtl1> children1 = new ArrayList<>();

	public void setChildren1(List<QlnvTtinHdongHangDtl1> children1) {
		this.children1.clear();
		for (QlnvTtinHdongHangDtl1 child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(QlnvTtinHdongHangDtl1 child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<QlnvTtinHdongHangDtl2> children2 = new ArrayList<>();

	public void setChildren2(List<QlnvTtinHdongHangDtl2> children2) {
		this.children2.clear();
		for (QlnvTtinHdongHangDtl2 child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(QlnvTtinHdongHangDtl2 child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

}
