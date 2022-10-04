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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = HhDxKhLcntThopHdr.TABLE_NAME)
@Data
public class HhDxKhLcntThopHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DX_KHLCNT_THOP_HDR";

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_THOP_HDR_SEQ")
//	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_THOP_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_THOP_HDR_SEQ")
	private Long id;

	@Temporal(TemporalType.DATE)
	Date ngayThop;

	String loaiVthh;
	@Transient
	String tenLoaiVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String moTaHangHoa;
	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;

	@Transient
	String tenHthucLcnt;
	@Transient
	String tenPthucLcnt;
	@Transient
	String tenLoaiHdong;
	@Transient
	String tenNguonVon;
	@Transient
	Long phuongAnId;

	@Temporal(TemporalType.DATE)
	Date tgianBdauTchucTu;
	@Temporal(TemporalType.DATE)
	Date tgianBdauTchucDen;

	@Temporal(TemporalType.DATE)
	Date tgianMthauTu;
	@Temporal(TemporalType.DATE)
	Date tgianMthauDen;

	@Temporal(TemporalType.DATE)
	Date tgianDthauTu;
	@Temporal(TemporalType.DATE)
	Date tgianDthauDen;

	@Temporal(TemporalType.DATE)
	Date tgianNhangTu;
	@Temporal(TemporalType.DATE)
	Date tgianNhangDen;

	@Temporal(TemporalType.DATE)
	Date ngayTao;

	String noiDung;
	String ghiChu;
	String nguoiTao;
	Integer namKhoach;
	String trangThai;
	@Transient
	String tenTrangThai;
	String tchuanCluong;

	@Transient
	private List<HhDxKhLcntThopDtl> hhDxKhLcntThopDtlList = new ArrayList<>();
}
