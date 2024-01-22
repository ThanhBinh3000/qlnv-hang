package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NH_PHIEU_KNGHIEM_CLUONG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuKnghiemCluongHang extends TrangThaiBaseEntity {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_KNGHIEM_CLUONG_SEQ")
//	@SequenceGenerator(sequenceName = "PHIEU_KNGHIEM_CLUONG_SEQ", allocationSize = 1, name = "PHIEU_KNGHIEM_CLUONG_SEQ")
	private Long id;
	private Integer nam;
	private String maDvi;
	private String maQhns;
	@Transient
	private String tenDvi;
	private String soBbLayMau;
	private String soQdGiaoNvNh;
	private String soBbNhapDayKho;
	private String soPhieuKiemNghiemCl;
	private Long idKyThuatVien;
	@Transient
	private String tenKyThuatVien;
	private Long idTruongPhong;
	@Transient
	private String tenTruongPhong;
	private String maDiemKho;
	@Transient
	private String tenDiemKho;
	private String maNhaKho;
	@Transient
	private String tenNhaKho;
	private String maNganKho;
	@Transient
	private String tenNganKho;
	private String maLoKho;
	@Transient
	private String tenLoKho;
	private Long idDdiemGiaoNvNh;
	private Long idQdGiaoNvNh;
	private String loaiVthh;
	@Transient
	private String tenLoaiVthh;
	private String cloaiVthh;
	@Transient
	private String tenCloaiVthh;
	private String moTaHangHoa;
	private String hthucBquan;
	private BigDecimal soLuongNhapDayKho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayNhapDayKho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMau;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayKnghiem;
	private String ketLuan;
	private String ketQuaDanhGia;
	@Transient
	List<KquaKnghiem> listKquaKngiem;
	@Transient
	private List<FileDinhKem> fileDinhKems;
}
