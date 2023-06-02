package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_QD_KHLCNT_DSGTHAU_CTIET_VT")
@Data
public class HhQdKhlcntDsgthauCtietVt {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_KHLCNT_CTIET_VT_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_KHLCNT_CTIET_VT_SEQ", allocationSize = 1, name = "HH_QD_KHLCNT_CTIET_VT_SEQ")
	private Long id;

	private String maDvi;

	@Transient
	private String tenDvi;

	private BigDecimal soLuong;

	private Long idGoiThauCtiet;
	String diaDiemNhap;
}