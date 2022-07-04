package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	private String maDvi;

	@Transient
	private String tenDvi;

	private BigDecimal donGia;

	private BigDecimal thanhTien;

	private String loaiVthh;

	private String cloaiVthh;

	private String dviTinh;

	private String hthucLcnt;

	private String pthucLcnt;

	private String loaiHdong;

	private String nguonVon;

	private String tgianBdauThien;

	private Integer tgianThienHd;

	String diaDiemNhap;

	@Transient
	private List<HhDxKhlcntDsgthauCtiet> children = new ArrayList<>();

}
