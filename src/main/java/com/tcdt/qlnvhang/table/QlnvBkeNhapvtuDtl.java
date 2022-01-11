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
@Table(name = "QLNV_BKE_NHAPVTU_DTL")
@Data
public class QlnvBkeNhapvtuDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BKE_NHAPVTU_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BKE_NHAPVTU_DTL_SEQ", allocationSize = 1, name = "QLNV_BKE_NHAPVTU_DTL_SEQ")
	private Long id;

	String soSerial;
	BigDecimal soLuong;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private QlnvBkeNhapvtuHdr parent;
}
