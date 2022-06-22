package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

	@Transient
	private List<HhQdKhlcntDsgthauCtiet> danhSachDiaDiemNhap = new ArrayList<>();

}
