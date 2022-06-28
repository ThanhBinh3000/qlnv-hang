package com.tcdt.qlnvhang.table;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = HhDthauDdiemNhap.TABLE_NAME)
@Data
public class HhDthauDdiemNhap {

	public static final String TABLE_NAME = "HH_DTHAU_DDIEM_NHAP";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauDdiemNhap.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauDdiemNhap.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauDdiemNhap.TABLE_NAME + "_SEQ")
	private Long id;

	@Transient
	private Long IdVirtual;

	private String maDvi;

	@Transient
	private String tenDvi;

	private String maDiemKho;

	@Transient
	private String tenDiemKho;

	private String diaDiemNhap;

	private BigDecimal soLuong;

	private BigDecimal donGia;

	private BigDecimal thanhTien;

	private Long idGoiThau;
}
