package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NH_BB_LAY_MAU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanLayMau extends TrangThaiBaseEntity implements Serializable  {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_LAY_MAU_SEQ")
//	@SequenceGenerator(sequenceName = "BB_LAY_MAU_SEQ", allocationSize = 1, name = "BB_LAY_MAU_SEQ")
	private Long id;

	@Column(name = "NAM")
	private Integer nam;

	@Column(name = "MA_DVI")
	private String maDvi;

	@Transient
	private String tenDvi;

	@Column(name = "SO_BIEN_BAN")
	private String soBienBan;

	@Column(name = "MA_QHNS")
	private String maQhns;

	@Column(name = "ID_QD_GIAO_NV_NH")
	private Long idQdGiaoNvNh;

	@Column(name = "SO_QD_GIAO_NV_NH")
	private String soQdGiaoNvNh;

	@Column(name = "NGAY_QD_GIAO_NV_NH")
	@Temporal(TemporalType.DATE)
	private Date ngayQdGiaoNvNh;

	@Column(name = "SO_HD")
	private String soHd;

	@Column(name = "SO_BB_NHAP_DAY_KHO")
	private String soBbNhapDayKho;

	@Column(name = "ID_BB_NHAP_DAY_KHO")
	private Long idBbNhapDayKho;

	@Column(name = "SO_BB_GUI_HANG")
	private String soBbGuiHang;

	@Column(name = "ID_BB_GUI_HANG")
	private Long idBbGuiHang;

	@Transient
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayNhapDayKho;

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

	@Column(name = "NGAY_LAY_MAU")
	@Temporal(TemporalType.DATE)
	private Date ngayLayMau;

	@Column(name = "DVI_KIEM_NGHIEM")
	private String dviKiemNghiem;

	@Column(name = "DIA_DIEM_LAY_MAU")
	private String diaDiemLayMau;

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

	@Transient
	private String tenNganLoKho;

	@Column(name = "MA_LO_KHO")
	private String maLoKho;

	@Column(name = "ID_DDIEM_GIAO_NV_NH")
	private Long idDdiemGiaoNvNh;

	@Transient
	private String tenLoKho;

	@Column(name = "SO_LUONG_MAU")
	private Integer soLuongMau;

	@Column(name = "PP_LAY_MAU")
	private String ppLayMau;

	@Column(name = "CHI_TIEU_KIEM_TRA")
	private String chiTieuKiemTra;

	@Column(name = "KET_QUA_NIEM_PHONG")
	private Boolean ketQuaNiemPhong;

	@Column(name = "LOAI_BIEN_BAN")
	private String loaiBienBan;
	private String truongBpKtbq;

	@Transient
	private List<BienBanLayMauCt> chiTiets = new ArrayList<>();

	@Transient
	private NhBbNhapDayKho bbNhapDayKho;

	@Transient
	private NhBienBanGuiHang bbGuiHang;
	@Transient
	private List<FileDinhKem> listFileDinhKemBb;
	@Transient
	private List<FileDinhKem> listFileDinhKemAnh;
	@Transient
	private List<FileDinhKem> listCcPhapLy;
}
