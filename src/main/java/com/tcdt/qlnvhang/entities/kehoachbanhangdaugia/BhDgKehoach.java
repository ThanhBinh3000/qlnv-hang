package com.tcdt.qlnvhang.entities.kehoachbanhangdaugia;

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
@Table(name = BhDgKehoach.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhDgKehoach extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1059131210401729547L;
	public static final String TABLE_NAME = "BH_DG_KEHOACH";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_DG_KEHOACH_SEQ")
	@SequenceGenerator(sequenceName = "BH_DG_KEHOACH_SEQ", allocationSize = 1, name = "BH_DG_KEHOACH_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "TRANG_THAI")
	private String trangThai;

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

	@Column(name = "LOAI_HANG_HOA")
	private String loaiHangHoa;

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

	@Column(name = "CAP_DV")
	private String capDv;

	@Column(name = "LOAI_VAT_TU_HANG_HOA")
	private String loaiVatTuHangHoa;

	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();

	@Transient
	private List<BhDgKhDiaDiemGiaoNhan> diaDiemGiaoNhanList = new ArrayList<>();

	@Transient
	private List<BhDgKhPhanLoTaiSan> phanLoTaiSanList = new ArrayList<>();
}
