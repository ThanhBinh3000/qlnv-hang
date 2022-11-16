package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
	private Long idTruongPhong;
	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maLoKho;
	private String idDdiemGiaoNvNh;
	private String loaiVthh;
	private String cloaiVthh;
	private String motaHangHoa;
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
}
