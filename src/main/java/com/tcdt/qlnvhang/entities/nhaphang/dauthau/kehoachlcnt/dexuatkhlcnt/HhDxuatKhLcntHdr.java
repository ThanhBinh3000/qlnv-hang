package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
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
	String tenLoaiVthh;
	String soQd;
	String trichYeu;
	String maDvi;
	@Transient
	String tenDvi;
	@Transient
	String tenDviLapDx;
	@Transient
	String tenDviCha;
	String trangThai;
	@Transient
	String tenTrangThai;
	@Transient
	String tenTrangThaiTh;
	@Temporal(TemporalType.DATE)
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
	String nguoiPduyet;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	Integer namKhoach;
	Integer namSxuat;
	Integer namThuHoach;
	String vu;
	String thuHoachVu;
	String ctietTccl;
	String ghiChu;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String moTaHangHoa;
	String tenDuAn;
	BigDecimal tongMucDt;
	BigDecimal tongMucDtDx;
	String loaiHdong;
	String tchuanCluong;
	String nguonVon;
	@Transient
	String tenNguonVon;
	String hthucLcnt;
	String pthucLcnt;
	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;
	Integer tgianThien;
	Integer tgianThienHd;
	@Temporal(TemporalType.DATE)
	Date tgianNhang;
	Integer gtriDthau;
	Integer gtriHdong;
	String dienGiai;
	String trangThaiTh;
	String loaiHinhNx;
	@Transient
	String tenLoaiHinhNx;
	String kieuNx;
	@Transient
	String tenKieuNx;
	@Transient
	String tenLoaiHdong;
	@Transient
	String tenPthucLcnt;
	@Transient
	String tenHthucLcnt;
	String diaChiDvi;
	BigDecimal donGiaVat;
	String dienGiaiTongMucDt;
	String quyMo;
	String cviecDaTh;
	String cviecKhongTh;
	String maDviLapDx;
	@Temporal(TemporalType.DATE)
	Date ngayKyQdPdGiaCuThe;
	String soQdPdGiaCuThe;
	@Transient
	String soQdPdKqLcnt;
	@Transient
	Long idQdPdKqLcnt;
	@Transient
	Integer soGthauTrung;

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
	private List<HhDxKhlcntDsgthau> dsGtDtlList = new ArrayList<>();
	@Transient
	private Long soGoiThau;

	@Transient
	private List<HhDxuatKhLcntCcxdgDtl> ccXdgDtlList = new ArrayList<>();

	@Transient
	private Long maTh;
	@Transient
	private BigDecimal qdGiaoChiTieuId;
	String ykienThamGia;
	Integer quy;
}
