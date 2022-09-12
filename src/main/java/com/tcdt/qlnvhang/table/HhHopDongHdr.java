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
import javax.persistence.Transient;

import com.tcdt.qlnvhang.entities.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;

@Entity
@Table(name = HhHopDongHdr.TABLE_NAME)
@Data
public class HhHopDongHdr extends BaseEntity implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_HOP_DONG_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhHopDongHdr.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhHopDongHdr.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhHopDongHdr.TABLE_NAME + "_SEQ")
	private Long id;
	private Long idGoiThau;
	private Long namKh;
	private Long donGiaVat;
	String soHd;
	String tenHd;
	String canCu;
	private Long canCuId;
//	String dviTrungThau;

	@Temporal(TemporalType.DATE)
	Date tuNgayHluc;

	@Temporal(TemporalType.DATE)
	Date denNgayHluc;

	Double soNgayThien;

	@Temporal(TemporalType.DATE)
	Date tuNgayTdo;

	@Temporal(TemporalType.DATE)
	Date denNgayTdo;

	Double soNgayTdo;
	String nuocSxuat;
	String tieuChuanCl;
	Double soLuong;
	Double gtriHdTrcVat;
	Double vat;
	Double gtriHdSauVat;
	String loaiVthh;
	@Transient
	String tenVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String moTaHangHoa;
	String loaiHd;

	@Temporal(TemporalType.DATE)
	Date ngayKy;

	String trangThai;
	@Transient
	String tenTrangThai;

	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ghiChu;
	String namKhoach;
	String maDvi;
	@Transient
	String tenDvi;

	String diaChi;

	Integer tgianBhanh;

	String mst;

	String sdt;

	String stk;

	String tenNguoiDdien;

	String chucVu;

	String idNthau;

	@Transient
	String tenNthau;

	@Temporal(TemporalType.DATE)
	Date tgianNkho;

	@Transient
	String donViTinh;

	@Transient
	private List<HhHopDongDdiemNhapKho> hhDdiemNhapKhoList = new ArrayList<>();

	@Transient
	private List<HhPhuLucHd> hhPhuLucHdongList = new ArrayList<>();

	@Transient
	private List<HhHopDongDtl> hhHopDongDtlList = new ArrayList<>();

	@Transient
	private List<HhDviLquan> hhDviLquanList = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhHopDongHdr.TABLE_NAME + "'")
	private List<FileDKemJoinHopDong> fileDinhKems = new ArrayList<>();

	public void setFileDinhKems(List<FileDKemJoinHopDong> children2) {
		this.fileDinhKems.clear();
		for (FileDKemJoinHopDong child2 : children2) {
			child2.setParent(this);
		}
		this.fileDinhKems.addAll(children2);
	}

	public void addFileDinhKems(FileDKemJoinHopDong child2) {
		child2.setParent(this);
		this.fileDinhKems.add(child2);
	}

}
