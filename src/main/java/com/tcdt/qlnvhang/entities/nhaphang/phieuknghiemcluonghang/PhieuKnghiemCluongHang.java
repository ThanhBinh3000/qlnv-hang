package com.tcdt.qlnvhang.entities.nhaphang.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
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
@Table(name = "NH_PHIEU_KNGHIEM_CLUONG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuKnghiemCluongHang extends TrangThaiBaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KNGHIEM_CLUONG_SEQ")
	@SequenceGenerator(sequenceName = "PHIEU_KNGHIEM_CLUONG_SEQ", allocationSize = 1, name = "PHIEU_KNGHIEM_CLUONG_SEQ")
	private Long id;
	private Long qdgnvnxId;
	private Long bbBanGiaoMauId;
	private String soPhieu;
	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;
	private String maVatTu;
	private String maVatTuCha;
	private BigDecimal sluongBquan;
	private String hthucBquan;
	private String thuKho;
	private LocalDate ngayNhapDay;
	private LocalDate ngayLayMau;
	private LocalDate ngayKnghiem;
	private String maDvi;
	private String capDvi;
	private String ketLuan;
	private String ketQuaDanhGia;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String trangThai;
	private Integer so;
	private Integer nam;
}
