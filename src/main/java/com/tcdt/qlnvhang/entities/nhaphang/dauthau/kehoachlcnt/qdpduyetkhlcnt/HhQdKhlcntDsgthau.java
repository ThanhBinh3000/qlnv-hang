package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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
	String lyDoHuy;
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
	private HhQdKhlcntDtl hhQdKhlcntDtl;

	@Transient
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	private List<HhQdKhlcntDsgthauCtiet> children = new ArrayList<>();

	String tenNhaThau;

	BigDecimal donGiaNhaThau;

	BigDecimal thanhTienNhaThau;
	BigDecimal soLuongTheoChiTieu;

	BigDecimal soLuongDaMua;

	Long idNhaThau;

	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(this.getTrangThai());
	}
}
