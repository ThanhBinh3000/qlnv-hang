package com.tcdt.qlnvhang.entities.dauthauvattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DT_VAT_TU_GOI_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTVatTuGoiThauVT extends BaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DT_VAT_TU_GOI_THAU_VT_SEQ")
	@SequenceGenerator(sequenceName = "DT_VAT_TU_GOI_THAU_VT_SEQ", allocationSize = 1, name = "DT_VAT_TU_GOI_THAU_VT_SEQ")
	private Long id;
	private Long ttdtVtId;
	private String tenGoiThau;
	private String maHhoa;
	private String tenHhoa;
	private BigDecimal soLuong;
	private BigDecimal donGia;
	private String pthucLcnt;
	private Integer tgianThienHdong;
	private String hthucHdong;
	private String ghiChu;
	private Integer stt;
}
