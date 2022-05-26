package com.tcdt.qlnvhang.entities.kehoachluachonnhathau;

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
@Table(name = "KH_LCNT_GOI_THAU_DDNHAP_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaDiemNhap  implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_LCNT_GOI_THAU_DDNHAP_VT_SEQ")
	@SequenceGenerator(sequenceName = "KH_LCNT_GOI_THAU_DDNHAP_VT_SEQ", allocationSize = 1, name = "KH_LCNT_GOI_THAU_DDNHAP_VT_SEQ")
	private Long id;
	private Long goiThauId;
	private String maDonVi;
	private Double soLuongNhap;
	private Integer stt;
}
