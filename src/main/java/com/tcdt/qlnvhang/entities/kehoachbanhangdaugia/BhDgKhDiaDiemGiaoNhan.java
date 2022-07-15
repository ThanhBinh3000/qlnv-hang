package com.tcdt.qlnvhang.entities.kehoachbanhangdaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = BhDgKhDiaDiemGiaoNhan.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhDgKhDiaDiemGiaoNhan implements Serializable {
	public static final String TABLE_NAME = "BH_DG_KH_DIA_DIEM_GIAO_NHAN";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_KH_DD_GIAO_NHAN_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_KH_DD_GIAO_NHAN_SEQ", allocationSize = 1, name = "BH_DG_KH_DD_GIAO_NHAN_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "BH_DG_KEHOACH_ID")
	private Long bhDgKehoachId;

	@Column(name = "STT")
	private Long stt;

	@Column(name = "TEN_CHI_CUC")
	private String tenChiCuc;

	@Column(name = "DIA_CHI")
	private String diaChi;

	@Column(name = "SO_LUONG")
	private BigDecimal soLuong;
}
