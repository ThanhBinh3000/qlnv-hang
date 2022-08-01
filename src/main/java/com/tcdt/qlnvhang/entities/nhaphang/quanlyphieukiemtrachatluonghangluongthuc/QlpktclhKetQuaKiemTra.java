package com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc;

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
public class QlpktclhKetQuaKiemTra {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KT_CHAT_LUONG_CT_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KT_CHAT_LUONG_CT_SEQ", allocationSize = 1, name = "PHIEU_KT_CHAT_LUONG_CT_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "STT")
	private Long stt;

	@Column(name = "TEN_CHI_TIEU")
	private String tenChiTieu;

	@Column(name = "TIEU_CHUAN")
	private String tieuChuan;

	@Column(name = "KET_QUA_KIEM_TRA")
	private String ketQuaKiemTra; // Ket qua phan tich

	@Column(name = "PHUONG_PHAP_XAC_DINH")
	private String phuongPhapXacDinh;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "PHIEU_KT_CHAT_LUONG_ID")
	private Long phieuKtChatLuongId;
}
