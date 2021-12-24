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
@Table(name = "QLNV_TTIN_HDONG_HANG_DTL1")
@Data
public class QlnvTtinHdongHangDtl1 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_TTIN_HDONG_HANG_DTL1_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_TTIN_HDONG_HANG_DTL1_SEQ", allocationSize = 1, name = "QLNV_TTIN_HDONG_HANG_DTL1_SEQ")
	private Long id;

	String maDvi;
	String maDiemKho;
	BigDecimal soLuong;
	String dviTinh;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private QlnvTtinHdongHangHdr parent;
}
