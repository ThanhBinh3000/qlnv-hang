package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "HH_DX_KHLCNT_DSGTHAU")
@Data
public class HhDxKhlcntDsgthau implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_DSGT_DTL_SEQ")
	private Long id;

	@Transient
	private Long idVirtual;

	private Long idDxKhlcnt;

	private String goiThau;

	private BigDecimal soLuong;

	private BigDecimal soLuongChiTieu;

	private BigDecimal soLuongDaMua;

	private String maDvi;

	@Transient
	private String tenDvi;

	private BigDecimal donGiaVat;

	private BigDecimal donGiaTamTinh;

	private BigDecimal thanhTien;

	private String loaiVthh;

	@Transient
	private String tenVthh;

	private String cloaiVthh;

	@Transient
	private String tenCloaiVthh;

	private String moTaHangHoa;

	private String dviTinh;

	private String hthucLcnt;

	@Transient
	private String tenHthucLcnt;

	private String pthucLcnt;

	@Transient
	private String tenPthucLcnt;

	private String loaiHdong;

	@Transient
	private String tenLoaiHdong;

	private String nguonVon;

	@Transient
	private String tenNguonVon;

	private String tgianBdauThien;

	private Integer tgianThienHd;

	String diaDiemNhap;

	@Transient
	private List<HhDxKhlcntDsgthauCtiet> children = new ArrayList<>();

	@Transient
	private String donGiaVatStr;
	@Transient
	private String thanhTienStr;
}
