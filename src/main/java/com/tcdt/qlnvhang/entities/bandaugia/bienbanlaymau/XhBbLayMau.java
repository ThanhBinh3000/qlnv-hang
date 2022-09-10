package com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhBbLayMau.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class XhBbLayMau extends TrangThaiBaseEntity implements Serializable {
	public static final String TABLE_NAME = "XH_BB_LAY_MAU";
	private static final long serialVersionUID = -7021660593183667859L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_SEQ")
	@SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "QDGNVX_ID")
	private Long qdgnvxId;

	@Column(name = "SO_BIEN_BAN")
	private String soBienBan;

	@Column(name = "DON_VI_KIEM_NGHIEM")
	private String donViKiemNghiem;

	@Column(name = "NGAY_LAY_MAU")
	private LocalDate ngayLayMau;

	@Column(name = "DIA_DIEM_LAY_MAU")
	private String diaDiemLayMau;

	@Column(name = "MA_VAT_TU_CHA")
	private String maVatTuCha;

	@Column(name = "MA_VAT_TU")
	private String maVatTu;

	@Column(name = "MA_DIEM_KHO")
	private String maDiemKho;

	@Column(name = "MA_NHA_KHO")
	private String maNhaKho;

	@Column(name = "MA_NGAN_KHO")
	private String maNganKho;

	@Column(name = "MA_NGAN_LO")
	private String maNganLo;

	@Column(name = "SO_LUONG_MAU")
	private BigDecimal soLuongMau;

	@Column(name = "PP_LAY_MAU")
	private String ppLayMau;

	@Column(name = "CHI_TIEU_KIEM_TRA")
	private String chiTieuKiemTra;

	@Column(name = "MA_DVI")
	private String maDvi;

	@Column(name = "CAP_DVI")
	private String capDvi;

	@Column(name = "KET_QUA_NIEM_PHONG")
	private String ketQuaNiemPhong;

	@Column(name = "SO")
	private Integer so;

	@Column(name = "NAM")
	private Integer nam;

	@Column(name = "LOAI_VTHH")
	private String loaiVthh;

	@Column(name = "HOP_DONG_ID")
	private Long hopDongId;


	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();

	@Transient
	private List<XhBbLayMauCt> chiTietList = new ArrayList<>();

}
