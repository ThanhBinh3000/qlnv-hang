package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

@Entity
@Table(name = "HH_DC_DX_LCNT_DTL")
@Data
public class HhDchinhDxKhLcntDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_QD_LCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_QD_LCNT_DTL_SEQ", allocationSize = 1, name = "QLNV_QD_LCNT_DTL_SEQ")
	private Long id;
	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	String maDiemKho;
	String diaDiemNhap;
	BigDecimal donGia;
	BigDecimal thanhTien;

	String loaiVthh;
	String cloaiVthh;
	String dviTinh;
	String hthucLcnt;
	@Transient
	String tenHthucLcnt;
	String pthucLcnt;
	@Transient
	String tenPthucLcnt;
	String loaiHdong;
	String nguonVon;
	String tgianBdauLcnt;
	String tchuanCluong;
	Integer tgianThienHd;
	private Long idHdr;

	@Transient
	private List<HhDchinhDxKhLcntDsgthauCtiet> danhSachDiaDiepNhap = new ArrayList<>();

	private Long idDxDcHdr;

	@Transient
	String tenDvi;

	String soDxuat;

	@Temporal(TemporalType.DATE)
	Date ngayDxuat;

	String tenDuAn;

	BigDecimal tongTien;

	Long soGthau;

	Long namKhoach;
	@Temporal(TemporalType.DATE)
	Date tgianBdauTchuc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;
	@Temporal(TemporalType.DATE)
	Date tgianNhang;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthauTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthauTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMoHoSoTime;
	BigDecimal giaBanHoSo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMoHoSo;
	Long idHhQdKhlcntDtl;
	@Transient
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	private List<HhDchinhDxKhLcntDsgthau> children = new ArrayList<>();

}
