package com.tcdt.qlnvhang.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QDKHLCNT_GOI_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuyetDinhKHLCNTGoiThauVatTu extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QDKHLCNT_GOI_THAU_VT_SEQ")
	@SequenceGenerator(sequenceName = "QDKHLCNT_GOI_THAU_VT_SEQ", allocationSize = 1, name = "QDKHLCNT_GOI_THAU_VT_SEQ")
	private Long id;
	private String tenGoiThau;
	private BigDecimal giaGoiThau;
	private Long nguonVonId;
	private Long hinhThucLcntId;
	private Long phuongThucLcntId;
	private String tgBatDau;
	private String loaiHopDongId;
	private String tgThHopDong;
	private Long quyetDinhId;
}