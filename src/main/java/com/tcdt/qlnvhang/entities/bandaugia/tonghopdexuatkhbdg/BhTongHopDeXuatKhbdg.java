package com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = BhTongHopDeXuatKhbdg.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhTongHopDeXuatKhbdg extends BaseEntity implements Serializable {
	public static final String TABLE_NAME = "BH_TONG_HOP_DE_XUAT_KHBDG";
	private static final long serialVersionUID = -3618984778480462998L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_TONG_HOP_DE_XUAT_KHBDG_SEQ")
	@SequenceGenerator(sequenceName = "BH_TONG_HOP_DE_XUAT_KHBDG_SEQ", allocationSize = 1, name = "BH_TONG_HOP_DE_XUAT_KHBDG_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAM_KE_HOACH")
	private Integer namKeHoach;

	@Column(name = "LOAI_HANG_HOA")
	private String loaiHangHoa;

	@Column(name = "NGAY_KY_TU_NGAY")
	private LocalDate ngayKyTuNgay;

	@Column(name = "NGAY_KY_DEN_NGAY")
	private LocalDate ngayKyDenNgay;

	@Column(name = "NGAY_TONG_HOP")
	private LocalDate ngayTongHop;

	@Column(name = "NOI_DUNG_TONG_HOP")
	private String noiDungTongHop;

	@Column(name = "TG_DU_KIEN_TCBDG_TU_NGAY")
	private LocalDate tgDuKienTcbdgTuNgay;

	@Column(name = "TG_DU_KIEN_TCBDG_DEN_NGAY")
	private LocalDate tgDuKienTcbdgDenNgay;

	@Column(name = "GHI_CHU")
	private String ghiChu;

	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "CAP_DON_VI")
	private String capDonVi;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "MA_TONG_HOP")
	private String maTongHop;

	@Column(name = "QD_PHE_DUYET_KHBDG_ID")
	private Long qdPheDuyetKhbdgId;
}
