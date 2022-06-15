package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "HH_DX_KHLCNT_DSACH_GTHAU_DTL")
@Data
public class HhDxuatKhLcntDsgtDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ")
	private Long id;

	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	String maDiemKho;
	String diaDiemNhap;
	BigDecimal donGia;
	BigDecimal thanhTien;
	String shgt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_KH_HDR",nullable=false)
	@JsonBackReference
	private HhDxuatKhLcntHdr parent;

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
	private List<HhDxuatKhLcntVtuDtlCtiet> danhSachDiaDiepNhap = new ArrayList<>();

}
