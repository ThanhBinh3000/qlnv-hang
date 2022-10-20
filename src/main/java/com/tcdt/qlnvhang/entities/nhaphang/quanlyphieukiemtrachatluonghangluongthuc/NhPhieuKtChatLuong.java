package com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NH_PHIEU_KT_CHAT_LUONG")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhPhieuKtChatLuong extends TrangThaiBaseEntity implements Serializable {
	private static final long serialVersionUID = -5114185021472069821L;
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KT_CHAT_LUONG_SEQ")
//	@SequenceGenerator(sequenceName = "PHIEU_KT_CHAT_LUONG_SEQ", allocationSize = 1, name = "PHIEU_KT_CHAT_LUONG_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAM")
	private Integer nam;

	@Column(name = "MA_DVI")
	private String maDvi;

	@Transient
	private String tenDvi;

	@Column(name = "SO_PHIEU")
	private String soPhieu;

	@Column(name = "MA_QHNS")
	private String maQhns;

	@Column(name = "ID_QD_GIAO_NV_NH")
	private Long idQdGiaoNvNh;

	@Column(name = "SO_QD_GIAO_NV_NH")
	private String soQdGiaoNvNh;

	@Column(name = "NGAY_QD_GIAO_NV_NH")
	private LocalDate ngayQdGiaoNvNh;

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;

	@Transient
	private String tenLoaiVthh;

	@Column(name = "CLOAI_VTHH")
	private String cloaiVthh;

	@Transient
	private String tenCloaiVthh;

	@Column(name = "MO_TA_HANG_HOA")
	private String moTaHangHoa;

	@Column(name = "SO_HD")
	private String soHd;

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

	@Column(name = "NGUOI_GIAO_HANG")
	private String nguoiGiaoHang;

	@Column(name = "CMT_NGUOI_GIAO_HANG")
	private String cmtNguoiGiaoHang;

	@Column(name = "DON_VI_GIAO_HANG")
	private String donViGiaoHang;

	@Column(name = "DIA_CHI")
	private String diaChi;

	@Column(name = "BIEN_SO_XE")
	private String bienSoXe;

	@Column(name = "SO_LUONG_DE_NGHI_KT")
	private Double soLuongDeNghiKt;

	@Column(name = "SO_LUONG_NHAP_KHO")
	private Double soLuongNhapKho;

	@Column(name = "SO_CHUNG_THU_GIAM_DINH")
	private String soChungThuGiamDinh;

	@Column(name = "NGAY_GDINH")
	private LocalDate ngayGdinh;

	@Column(name = "TCHUC_GDINH")
	private String tchucGdinh;

	@Column(name = "KET_LUAN")
	private String ketLuan;

	@Column(name = "KQ_DANH_GIA")
	private String kqDanhGia;

	@Column(name = "ID_DDIEM_GIAO_NV_NH")
	private Long idDdiemGiaoNvNh;

	@Transient
	private List<QlpktclhKetQuaKiemTra> ketQuaKiemTra = new ArrayList<>();
}
