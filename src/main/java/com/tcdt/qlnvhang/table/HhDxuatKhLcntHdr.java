package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;

import lombok.Data;

@Entity
@Table(name = HhDxuatKhLcntHdr.TABLE_NAME)
@Data
public class HhDxuatKhLcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DX_KHLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_HDR_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_HDR_SEQ")
	private Long id;
	String soDxuat;
	String loaiVthh;
	@Transient
	String tenVthh;
	String soQd;
	String trichYeu;
	String maDvi;
	@Transient
	String tenDvi;
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
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	Long namKhoach;
	String ghiChu;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String maVtu;
	@Transient
	String tenVtu;
	boolean lastest;
	String tenDuAn;
	BigDecimal tongMucDt;
	String loaiHdong;
	String tchuanCluong;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;
	@Temporal(TemporalType.DATE)
	Date tgianMthau;
	@Temporal(TemporalType.DATE)
	Date tgianDthau;
	Integer tgianThienHd;
	@Temporal(TemporalType.DATE)
	Date tgianNhang;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDxuatKhLcntHdr.TABLE_NAME + "'")
	private List<FileDKemJoinDxKhLcntHdr> fileDinhKems = new ArrayList<>();

	public void setFileDinhKems(List<FileDKemJoinDxKhLcntHdr> children) {
		this.fileDinhKems.clear();
		for (FileDKemJoinDxKhLcntHdr child : children) {
			child.setParent(this);
		}
		this.fileDinhKems.addAll(children);
	}

	@Transient
	private List<HhDxuatKhLcntDsgtDtl> dsGtDtlList = new ArrayList<>();

	@Transient
	private List<HhDxuatKhLcntCcxdgDtl> ccXdgDtlList = new ArrayList<>();

}
