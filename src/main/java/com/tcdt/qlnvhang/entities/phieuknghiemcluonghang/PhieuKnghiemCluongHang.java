package com.tcdt.qlnvhang.entities.phieuknghiemcluonghang;

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
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PHIEU_KNGHIEM_CLUONG_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuKnghiemCluongHang extends BaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KNGHIEM_CLUONG_HANG_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KNGHIEM_CLUONG_HANG_SEQ", allocationSize = 1, name = "PHIEU_KNGHIEM_CLUONG_HANG_SEQ")
	private Long id;
	private String soPhieu;
	private LocalDate ngayLayMau;
	private LocalDate ngayKnghiem;
	private String maNgan;
	private String tenNgan;
	private String maKho;
	private String tenKho;
	private String maHhoa;
	private String tenHhoa;
	private String soBbanKthucNhap;
	private LocalDate ngayNhapDay;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String ddiemBquan;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String trangThai;
	private String ldoTchoi;
	private String maDonVi;
	private String capDonVi;
}
