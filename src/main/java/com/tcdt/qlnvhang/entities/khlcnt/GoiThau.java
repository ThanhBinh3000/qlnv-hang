package com.tcdt.qlnvhang.entities.khlcnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "KH_LCNT_GOI_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoiThau implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_LCNT_GOI_THAU_VT_SEQ")
	@SequenceGenerator(sequenceName = "KH_LCNT_GOI_THAU_VT_SEQ", allocationSize = 1, name = "KH_LCNT_GOI_THAU_VT_SEQ")
	private Long id;
	private Long khLcntId;
	private String tenGoiThau;
	private String maVatTu;
	private String tenVatTu;
	private Long vatTuId;
	private Double soLuong;
	private Double donGia;
	private Integer stt;
}
