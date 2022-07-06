package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;
	String tgianBdauLcnt;
	Integer tgianThienHd;
	private Long idHdr;


	@Transient
	private List<HhDchinhDxKhLcntDtlCtiet> danhSachDiaDiepNhap = new ArrayList<>();

}
