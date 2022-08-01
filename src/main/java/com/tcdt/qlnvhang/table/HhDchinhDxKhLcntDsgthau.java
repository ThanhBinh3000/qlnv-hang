package com.tcdt.qlnvhang.table;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_DC_DX_LCNT_DSGTHAU")
@Data
public class HhDchinhDxKhLcntDsgthau implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_DX_LCNT_DSGTHAU_SEQ")
	@SequenceGenerator(sequenceName = "HH_DC_DX_LCNT_DSGTHAU_SEQ", allocationSize = 1, name = "HH_DC_DX_LCNT_DSGTHAU_SEQ")
	private Long id;
	private Long idDxDcDtl;
	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	@Transient
	String tenDvi;
	BigDecimal donGia;
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
	String tenVthh;
	@Transient
	String tenHthucLcnt;
	@Transient
	String tenPthucLcnt;
	@Transient
	String tenLoaiHdong;
	@Transient
	String tenNguonVon;

	@Transient
	private HhQdKhlcntDtl hhQdKhlcntDtl;

	@Transient
	private HhQdKhlcntHdr hhQdKhlcntHdr;

	@Transient
	private List<HhQdKhlcntDsgthauCtiet> children = new ArrayList<>();

}
