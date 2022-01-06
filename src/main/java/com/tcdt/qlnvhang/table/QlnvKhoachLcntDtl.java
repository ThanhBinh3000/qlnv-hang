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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_KHOACH_LCNT_DTL")
@Data
public class QlnvKhoachLcntDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KHOACH_LCNT_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KHOACH_LCNT_DTL_SEQ", allocationSize = 1, name = "QLNV_KHOACH_LCNT_DTL_SEQ")
	private Long id;
	String maDvi;
	String phanThau;
	BigDecimal soLuong;
	String dviTinh;
	BigDecimal giaDkien;
	BigDecimal giaDkienVat;
	String diaChi;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
    private QlnvKhoachLcntHdr header;
}
