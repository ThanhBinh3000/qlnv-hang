package com.tcdt.qlnvhang.entities.kehoachbanhangdaugia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = BhDgKhPhanLoTaiSan.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhDgKhPhanLoTaiSan {
	public static final String TABLE_NAME = "BH_DG_KH_PHAN_LO_TAI_SAN";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ", allocationSize = 1, name = "BH_DG_KH_PHAN_LO_TAI_SAN_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "STT")
	private Long stt;

	@Column(name = "DIEM_KHO")
	private String diemKho;

	@Column(name = "NGAN_KHO")
	private String nganKho;

	@Column(name = "LO_KHO")
	private String loKho;

	@Column(name = "CHUNG_LOAI_HH")
	private String chungLoaiHh;

	@Column(name = "MA_DV_TAI_SAN")
	private String maDvTaiSan;

	@Column(name = "TON_KHO")
	private BigDecimal tonKho;

	@Column(name = "SO_LUONG")
	private BigDecimal soLuong;

	@Column(name = "DON_GIA")
	private BigDecimal donGia;

	@Column(name = "CHI_CUC")
	private String chiCuc;

	@Column(name = "BH_DG_KEHOACH_ID")
	private Long bhDgKehoachId;
}
