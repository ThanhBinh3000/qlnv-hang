package com.tcdt.qlnvhang.table;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "HH_QD_KHLCNT_DSGTHAU_CTIET")
@Data
public class HhQdKhlcntDsgthauCtiet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	private Long id;
	private Integer soLuong;
	private String tenDvi;
	private String maDvi;
	private Long idGoiThau;
}