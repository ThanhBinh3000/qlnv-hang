package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ThongTinDauThauRes {

	private Long id;
	private Long qdKhlcntId;
	private String soDxuat;
	private LocalDate ngayDxuat;
	private String soQdinhPduyetKhlcnt;
	private String ngayPduyetKhlcnt;
	private String tenDuAn;
	private String tenChuDtu;
	private String benMoiThau;
	private String hthucDuThau;
	private LocalDate tgianDongThau;
	private Integer tgianHlucEhsmt;
	private BigDecimal tongMucDtu;
	private String ghiChu;

	private Long soGoiThau;
	private Long tongGoiThau;
	private List<DTVatTuGoiThauVTRes> goiThauList;

	private Long idQdHdr;
	private Long idQdDtl;
	private Long idGt;

	private Long idNhaThau;
	private String goiThau;
	private String maDvi;
	private String soQdPdKhlcnt;
	private Date ngayQd;
	private String trichYeu;
	private String loaiVthh;
	private String cloaiVthh;
	private String tenVthh;
	private String tenCloaiVthh;
	private BigDecimal thanhGiaGoiThau;
	private String trangThai;

	private String tenDvi;
	private String nguonVon;

	private String tenNguonVon;

	private String tenNhaThau;
	private Integer tgianThienHd;

	private String loaiHdong;

	private String tenLoaiHdong;

	private BigDecimal donGiaTrcVat;

	private Integer vat;

	private BigDecimal soLuong;


	public ThongTinDauThauRes(Long idQdHdr, Long idQdDtl, Long idGt,Long idNhaThau, String goiThau, String maDvi, String soQdPdKhlcnt,Date ngayQd, String trichYeu, String loaiVthh, String cloaiVthh, BigDecimal thanhGiaGoiThau, String trangThai,String nguonVon,Integer tgianThienHd,String loaiHdong, Integer vat, BigDecimal soLuong, BigDecimal donGiaTrcVat) {
		this.idQdHdr = idQdHdr;
		this.idQdDtl = idQdDtl;
		this.idGt = idGt;
		this.idNhaThau = idNhaThau;
		this.goiThau = goiThau;
		this.maDvi = maDvi;
		this.soQdPdKhlcnt = soQdPdKhlcnt;
		this.ngayQd = ngayQd;
		this.trichYeu = trichYeu;
		this.loaiVthh = loaiVthh;
		this.cloaiVthh = cloaiVthh;
		this.thanhGiaGoiThau = thanhGiaGoiThau;
		this.trangThai = trangThai;
		this.nguonVon = nguonVon;
		this.tgianThienHd = tgianThienHd;
		this.loaiHdong = loaiHdong;
		this.vat = vat;
		this.soLuong = soLuong;
		this.donGiaTrcVat = donGiaTrcVat;
	}
}
