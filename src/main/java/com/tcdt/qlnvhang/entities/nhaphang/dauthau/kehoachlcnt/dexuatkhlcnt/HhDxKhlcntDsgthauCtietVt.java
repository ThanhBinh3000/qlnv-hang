package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HH_DX_KHLCNT_DSGTHAU_CTIET_VT")
@Data
public class HhDxKhlcntDsgthauCtietVt {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_CTIET_VT_SEQ")
	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_CTIET_VT_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_CTIET_VT_SEQ")
	private Long id;

	Integer soLuong;

	String maDvi;

	Long idGoiThauCtiet;

	@Transient
	String tenDvi;

}
