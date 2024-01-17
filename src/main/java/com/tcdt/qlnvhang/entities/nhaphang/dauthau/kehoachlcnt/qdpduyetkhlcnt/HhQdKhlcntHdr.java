package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai.QdPdHsmt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.response.chotdulieu.QthtChotGiaInfoRes;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdKhlcntHdr;

import lombok.Data;

@Entity
@Table(name = HhQdKhlcntHdr.TABLE_NAME)
@Data
public class HhQdKhlcntHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_KHLCNT_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_HDR_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_HDR_SEQ")
	private Long id;

	String soQd;

	String soQdPdKqLcnt;

	@Temporal(TemporalType.DATE)
	Date ngayQd;

	Long idThHdr;

	Long idTrHdr;

	String trangThai;

	Date ngaySua;

	String nguoiSua;

	String ldoTuchoi;

	Date ngayGuiDuyet;

	String nguoiGuiDuyet;
	@Temporal(TemporalType.DATE)
	Date ngayPduyet;
	@Transient
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE)
	Date ngayPdQdPdkqKhlcnt;

	String nguoiPduyet;

	String ghiChu;

	String loaiVthh;

	String cloaiVthh;

	String hthucLcnt;

	String pthucLcnt;

	String loaiHdong;

	String nguonVon;

	@Transient
	String tenLoaiVthh;

	@Transient
	String tenCloaiVthh;

	@Transient
	String tenHthucLcnt;

	@Transient
	String tenPthucLcnt;

	@Transient
	String tenLoaiHdong;

	@Transient
	String tenNguonVon;
	@Transient
	String tenKieuNx;
	@Transient
	String tenLoaiHinhNx;

	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@Temporal(TemporalType.DATE)
	Date ngayTao;

	String nguoiTao;

	String trichYeu;

	Integer namKhoach;

	Integer tgianThien;
	Integer tgianThienHd;
	Integer vat;

	Integer gtriDthau;

	Integer gtriHdong;

	String dienGiai;

	String ykienThamGia;

	@Temporal(TemporalType.DATE)
	Date ngayHluc;

	String soTrHdr;

	Boolean lastest;
	Boolean dieuChinh;

	String soQdCc;

	String phanLoai;

	Long idGoc;

	String maDvi;

	String trangThaiDt;
	String noiDungQd;
	String tenDuAn;
	String dienGiaiTongMucDt;
	String quyMo;
	BigDecimal tongMucDtDx;
	BigDecimal tongMucDt;

	@Transient
	String tenDvi;

	@Transient
	private List<HhQdKhlcntDtl> children = new ArrayList<>();
	@Transient
	private List<HhQdKhlcntDsgthau> dsGthau = new ArrayList<>();
	@Transient
	private HhDxuatKhLcntHdr dxKhlcntHdr;
	@Transient
	private HhDchinhDxKhLcntHdr dchinhDxKhLcntHdr ;
	@Transient
	BigDecimal tongTien;
	@Transient
	Long soGthau;
	@Transient
	Long soGthauTrung;
	@Transient
	Long soGthauTruot;
	@Transient
	String tenTrangThai;
	@Transient
	String tenTrangThaiDt;
	@Transient
	String soDxuatKhlcnt;
	@Transient
	String soQdDc;
	@Transient
	Long soHdDaKy;
	@Transient
	private QdPdHsmt qdPdHsmt;
	@Transient
	private List<FileDinhKem> listCcPhapLy;
	@Transient
	private List<FileDinhKem> fileDinhKems;
	@Transient
	private Integer lanDieuChinh;
	@Transient
	private List<Long> listIdGthau = new ArrayList<>();
	@Transient
	private QthtChotGiaInfoRes qthtChotGiaInfoRes;

	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTenById(this.getTrangThai());
	}

	public String getTenTrangThaiDt() {
		return NhapXuatHangTrangThaiEnum.getTenById(this.getTrangThaiDt());
	}
}
