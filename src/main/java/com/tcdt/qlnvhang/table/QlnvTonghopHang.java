package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;

@Entity
@Table(name = "QLNV_TONGHOP_HANG")
@Data
public class QlnvTonghopHang implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TONGHOP_HANG_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TONGHOP_HANG_SEQ", allocationSize = 1, name = "QLNV_TONGHOP_HANG_SEQ")
	private Long id;

	Long idPhieuNhapxuat;
	String tenSo;
	String maDvi;
	String maKho;
	String maNgan;
	String maLo;
	String maHanghoa;
	String dviTinh;
	BigDecimal tongNhap;
	BigDecimal tongXuat;
	BigDecimal ton;
	@Version
	Long version;
}
