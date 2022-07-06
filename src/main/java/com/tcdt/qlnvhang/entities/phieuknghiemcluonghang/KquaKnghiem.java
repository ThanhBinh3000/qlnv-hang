package com.tcdt.qlnvhang.entities.phieuknghiemcluonghang;

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

@Entity
@Table(name = "NH_PHIEU_KNGHIEM_CLUONG_CT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KquaKnghiem {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KNGHIEM_CLUONG_CT_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KNGHIEM_CLUONG_CT_SEQ", allocationSize = 1, name = "PHIEU_KNGHIEM_CLUONG_CT_SEQ")
	private Long id;
	private Long phieuKnghiemId;
	private Integer stt;
	private String tenCtieu;
	private String kquaKtra;
	private String pphapXdinh;
	private String chiSoChatLuong;
}
