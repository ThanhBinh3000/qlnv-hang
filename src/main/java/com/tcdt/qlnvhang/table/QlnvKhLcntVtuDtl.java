package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "QLNV_KH_LCNT_VTU_DTL")
@Data
public class QlnvKhLcntVtuDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_SEQ")
	private Long id;
	String tenGoiThau;
	Integer soLuong;
	BigDecimal donGia;
	@Lob
	String doc;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kh_lcnt_vtu_id")
    private QlnvKhLcntVtuHdr header;
}
