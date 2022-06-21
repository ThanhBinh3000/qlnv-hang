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
	@Transient
	Long idVirtual;
	private Long idDxKhlcnt;
	String goiThau;
	BigDecimal soLuong;
	String maDvi;
	@Transient
	String tenDvi;
	String maDiemKho;
	@Transient
	String tenDiemKho;
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
	List<HhDxuatKhLcntDsgtDtl> children = new ArrayList<>();

	@Transient
	private List<HhDxuatKhLcntVtuDtlCtiet> danhSachDiaDiepNhap = new ArrayList<>();

}
