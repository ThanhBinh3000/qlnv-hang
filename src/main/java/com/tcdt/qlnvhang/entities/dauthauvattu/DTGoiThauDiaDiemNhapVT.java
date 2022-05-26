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
@Table(name = "DT_GOI_THAU_DIA_DIEM_NHAP_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTGoiThauDiaDiemNhapVT extends BaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DT_GOI_THAU_DDN_VT_SEQ")
	@SequenceGenerator(sequenceName = "DT_GOI_THAU_DDN_VT_SEQ", allocationSize = 1, name = "DT_GOI_THAU_DDN_VT_SEQ")
	private Long id;
	private Long dtvtGoiThauId;
	private Integer stt;
	private String maDonVi;
	private Long donViId;
	private BigDecimal soLuongNhap;
}
