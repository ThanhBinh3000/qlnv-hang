package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "HH_DX_KHLCNT_DSGTHAU_CTIET_VT1")
@Data
public class HhDxKhlcntDsgthauCtietVt1 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_CTIET_VT1_SEQ")
	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_CTIET_VT1_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_CTIET_VT1_SEQ")
	private Long id;

	Integer soLuong;

	String maDvi;

	Long idGoiThauCtietVt;

	@Transient
	String tenDvi;

}
