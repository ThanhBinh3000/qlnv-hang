package com.tcdt.qlnvhang.table;

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
@Table(name = "QLNV_KH_LCNT_VTU_DTL_CTIET")
@Data
public class QlnvKhLcntVtuDtlCtiet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ", allocationSize = 1, name = "QLNV_KH_LCNT_VTU_DTL_CTIET_SEQ")
	private Long id;
	Integer soLuong;
	String tenDvi;
	String maDvi;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dtl")
    private QlnvKhLcntVtuDtl parent;
	
}
