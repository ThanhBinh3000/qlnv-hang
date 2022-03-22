package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	String diaDiemNhap;
	BigDecimal donGia;
	BigDecimal thanhTien;
	String shgt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhDxuatKhLcntHdr parent;

}
