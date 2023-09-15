package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "NH_PHIEU_KT_CHAT_LUONG_CT")
public class NhPhieuKtChatLuongCt {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KT_CHAT_LUONG_CT_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KT_CHAT_LUONG_CT_SEQ", allocationSize = 1, name = "PHIEU_KT_CHAT_LUONG_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "TEN_TCHUAN")
	private String tenTchuan;

	@Column(name = "KET_QUA_KIEM_TRA")
	private String ketQuaKiemTra; // Ket qua phan tich

	@Column(name = "PHUONG_PHAP")
	private String phuongPhap;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "PHIEU_KT_CHAT_LUONG_ID")
	private Long phieuKtChatLuongId;

	@Column(name = "CHI_SO_NHAP")
	private String chiSoNhap;

	@Column(name = "KIEU")
	private String kieu;

	private String danhGia;

}
