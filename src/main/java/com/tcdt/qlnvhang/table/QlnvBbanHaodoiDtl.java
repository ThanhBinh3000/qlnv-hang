package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "QLNV_BBAN_HAODOI_DTL")
@Data
public class QlnvBbanHaodoiDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_BBAN_HAODOI_DTL_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_BBAN_HAODOI_DTL_SEQ", allocationSize = 1, name = "QLNV_BBAN_HAODOI_DTL_SEQ")
	private Long id;
	
	Date denNgayBquan;
	BigDecimal soLuong;
	String dviTinh;
	Double dmucHao;
	BigDecimal sluongHao;
	Date tuNgayBquan;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
    private QlnvBbanHaodoiHdr header;
}
