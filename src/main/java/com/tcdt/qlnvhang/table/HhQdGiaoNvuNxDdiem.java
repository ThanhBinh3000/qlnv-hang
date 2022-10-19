package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_QD_GIAO_NVU_NX_DDIEM")
@Data
public class HhQdGiaoNvuNxDdiem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NX_DDIEM_NHAP_SEQ")
	@SequenceGenerator(sequenceName = "QD_GIAO_NVU_NX_DDIEM_NHAP_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NX_DDIEM_NHAP_SEQ")
	private Long id;

	@Column(name = "ID_CT")
	private Long idCt;

	@Column(name = "MA_CUC")
	private String maCuc;

	@Transient
	private String tenCuc;

	@Column(name = "MA_CHI_CUC")
	private String maChiCuc;

	@Transient
	private String tenChiCuc;

	@Column(name = "MA_DIEM_KHO")
	private String maDiemKho;

	@Transient
	private String tenDiemKho;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;

	@Transient
	private String tenNhaKho;

	@Column(name = "MA_NGAN_KHO")
	private String maNganKho;

	@Transient
	private String tenNganKho;

	@Column(name = "MA_LO_KHO")
	private String maLoKho;

	@Transient
	private String tenLoKho;

	@Column(name="SO_LUONG")
	private BigDecimal soLuong;

	@Transient
    NhPhieuKtChatLuong phieuKtraCl;

	@Transient
	NhPhieuNhapKho phieuNhapKho;
}
