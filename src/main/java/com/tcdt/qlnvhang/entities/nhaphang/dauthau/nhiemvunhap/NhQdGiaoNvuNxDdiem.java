package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "NH_QD_GIAO_NVU_NX_DDIEM")
@Data
public class NhQdGiaoNvuNxDdiem implements Serializable {

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
	private String tenNganLoKho;

	@Transient
	private String tenLoKho;

	@Column(name="SO_LUONG")
	private BigDecimal soLuong;
	private BigDecimal slTon;

	@Column(name = "SO_LUONG_DIEM_KHO")
	BigDecimal soLuongDiemKho;

	@Transient
	List<NhPhieuKtChatLuong> listPhieuKtraCl;

	@Transient
	List<NhPhieuNhapKho> listPhieuNhapKho;

	@Transient
	List<NhBangKeCanHang> listBangKeCanHang;

	@Transient
	NhBbNhapDayKho bienBanNhapDayKho;

	@Transient
	BienBanLayMau bienBanLayMau;

	@Transient
	NhBienBanChuanBiKho bienBanChuanBiKho;

	@Transient
	NhPhieuNhapKhoTamGui phieuNhapKhoTamGui;

	@Transient
	NhBienBanGuiHang bienBanGuiHang;

	@Transient
	NhHoSoKyThuat hoSoKyThuat;

	@Transient
	List<NhBangKeVt> listBangKeVt;
	@Transient
	List<HhBbNghiemthuKlstHdr> listBbNtbqld;

	@Transient
	String soBbNtbqld;
}
