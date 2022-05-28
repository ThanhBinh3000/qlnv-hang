package com.tcdt.qlnvhang.entities.bbanlaymau;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BB_LAY_MAU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanLayMau extends BaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_LAY_MAU_SEQ")
	@SequenceGenerator(sequenceName = "BIEN_BAN_LAY_MAU_SEQ", allocationSize = 1, name = "BIEN_BAN_LAY_MAU_SEQ")
	private Long id;
	private String maHhoa;
	private String ccuQdinhGiaoNvuNhap;
	private String maKho;
	private String maLo;
	private String maNgan;
	private String soBban;
	private LocalDate ngayLapBban;
	private String maDviNhan;
	private String tenDdienNhan;
	private String cvuDdienNhan;
	private String maDviCcap;
	private String tenDdienCcap;
	private String cvuDdienCcap;
	private String canCu;
	private String ddiemKtra;
	private String sluongLmau;
	private String pphapLayMau;
	private String ctieuKtra;
	private String kquaNiemPhongMau;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String trangThai;
	private String ldoTchoi;
	private String maDonVi;
	private String capDonVi;
	private String maQhns;
	private String soHd;
	private LocalDate ngayLayMau;
	private String ddiemLayMau;
	private String ddienCucDtruNnuoc;
	private String tphongKthuatBquan;
	private String ddienBenNhan;
	private LocalDate ngayBgiaoMau;
	private String ddienCucDtruGiao;
	private String ddienDviTchucNhan;
	private String tinhTrang;
	private Integer soTrang;
}
