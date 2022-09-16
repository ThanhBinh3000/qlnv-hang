package com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = KeHoachBanDauGia.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeHoachBanDauGia extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1059131210401729547L;
	public static final String TABLE_NAME = "BH_DG_KEHOACH";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_KEHOACH_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_KEHOACH_SEQ", allocationSize = 1, name = "BH_DG_KEHOACH_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "TRANG_THAI")
	private String trangThai;
	@Transient
	private String tenTrangThai;

	@Column(name = "NAM_KE_HOACH")
	private Integer namKeHoach;

	@Column(name = "SO_KE_HOACH")
	private String soKeHoach;

	@Column(name = "TRICH_YEU")
	private String trichYeu;

	@Column(name = "NGAY_LAP_KE_HOACH")
	private LocalDate ngayLapKeHoach;

	@Column(name = "NGAY_KY")
	private LocalDate ngayKy;

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;
	@Transient
	private String tenLoaiVthh;

	@Column(name = "CLOAI_VTHH")
	private String cloaiVthh;
	@Transient
	private String tenCloaiVthh;

	@Column(name = "QD_GIAO_CHI_TIEU_ID")
	private Long qdGiaoChiTieuId;

	@Column(name = "TIEU_CHUAN_CHAT_LUONG")
	private String tieuChuanChatLuong;

	@Column(name = "SO_LUONG")
	private BigDecimal soLuong;

	@Column(name = "KHOAN_TIEN_DAT_TRUOC")
	private BigDecimal khoanTienDatTruoc;

	@Column(name = "LOAI_HOP_DONG")
	private String loaiHopDong;

	@Column(name = "THOI_GIAN_KY_HD")
	private String thoiGianKyHd;

	@Column(name = "TG_DK_TC_TU_NGAY")
	private LocalDate tgDkTcTuNgay;

	@Column(name = "TG_DK_TC_DEN_NGAY")
	private LocalDate tgDkTcDenNgay;

	@Column(name = "THOI_HAN_THANH_TOAN")
	private String thoiHanThanhToan;

	@Column(name = "PHUONG_THUC_THANH_TOAN")
	private String phuongThucThanhToan;

	@Column(name = "THONG_BAO_KH_BDG")
	private String thongBaoKhBdg;

	@Column(name = "PHUONG_THUC_GIAO_NHAN")
	private String phuongThucGiaoNhan;

	@Column(name = "THOI_HAN_GIAO_NHAN")
	private BigDecimal thoiHanGiaoNhan;

	@Column(name = "MA_DV")
	private String maDv;
	@Transient
	private String tenDonVi;

	@Column(name = "CAP_DV")
	private String capDv;


	@Column(name = "SO_QD_PHE_DUYET")
	private String soQuyetDinhPheDuyet;


	@Transient
	private String soQuyetDinhGiaoChiTieu;

	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();

	@Transient
	private List<BanDauGiaDiaDiemGiaoNhan> diaDiemGiaoNhanList = new ArrayList<>();

	@Transient
	private List<BanDauGiaPhanLoTaiSan> phanLoTaiSanList = new ArrayList<>();

	@Column(name = "THOI_GIAN_KY_HD_GC")
	private String thoiGianKyHopDongGhiChu;

	@Column(name = "THOI_HAN_THANH_TOAN_GC")
	private String thoiHanThanhToanGhiChu;

	@Column(name = "THOI_HAN_GIAO_NHAN_GC")
	private String ThoiHanGiaoNhanGhiChu;
//
//	@Transient
//	private String tenHangHoa;
}
