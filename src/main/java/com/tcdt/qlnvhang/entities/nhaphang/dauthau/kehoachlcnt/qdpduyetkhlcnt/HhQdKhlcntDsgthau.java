package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "HH_QD_KHLCNT_DSGTHAU")
@Data
public class HhQdKhlcntDsgthau implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_DSGTHAU_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_DSGTHAU_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_DSGTHAU_SEQ")
	private Long id;
	private Long idQdDtl;
	private Long idQdHdr;
	private Long idGthauDx;
	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	@Transient
	String tenDvi;
	BigDecimal donGiaVat;
	BigDecimal donGiaTamTinh;
	BigDecimal thanhTien;
	String loaiVthh;
	String cloaiVthh;
	String dviTinh;
	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;
	String tgianBdauLcnt;
	Integer tgianThienHd;
	String trangThai;
	String trangThaiDt;
	String dienGiaiNhaThau;
	String diaDiemNhap;

	String tgianBdauThien;

	@Transient
	String tenCloaiVthh;
	@Transient
	String tenLoaiVthh;
	@Transient
	String tenHthucLcnt;
	@Transient
	String tenPthucLcnt;
	@Transient
	String tenLoaiHdong;
	@Transient
	String tenNguonVon;

	@Transient
	String tenTrangThai;
	@Transient
	String tenTrangThaiDt;

	@Transient
	private HhQdKhlcntDtl hhQdKhlcntDtl;

	@Transient
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	HhQdPduyetKqlcntDtl kqlcntDtl;

	@Transient
	private List<HhQdKhlcntDsgthauCtiet> children = new ArrayList<>();
	@Transient
	private List<HhDthauNthauDuthau> dsNhaThauDthau = new ArrayList<>();
	String tenNhaThau;

	BigDecimal donGiaNhaThau;

	BigDecimal thanhTienNhaThau;
	BigDecimal soLuongTheoChiTieu;

	BigDecimal soLuongDaMua;

	Long idNhaThau;
	private String ghiChuTtdt;
	@Temporal(TemporalType.DATE)
	private Date tgianTrinhKqTcg;
	@Temporal(TemporalType.DATE)
	private Date tgianTrinhTtd;
	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();
	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(this.getTrangThai());
	}

	@Transient
	private String thanhTienStr;

	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;
	@Temporal(TemporalType.DATE)
	private Date tgianMthau;
	@Temporal(TemporalType.DATE)
	private Date tgianDthau;
	private Long idQdPdHsmt;
}
