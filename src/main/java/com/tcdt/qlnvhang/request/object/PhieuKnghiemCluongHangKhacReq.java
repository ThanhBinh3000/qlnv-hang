package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.KquaKnghiemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PhieuKnghiemCluongHangKhacReq extends BaseRequest {
	private Long id;
	private Integer nam;
	private String maDvi;
	private String maQhns;
	@Transient
	private String tenDvi;
	private String soBbLayMau;
	private Long idBbLayMau;
	private String soQdGiaoNvNh;
	private String soBbNhapDayKho;
	private String soPhieuKiemNghiemCl;
	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maLoKho;
	private Long idDdiemGiaoNvNh;
	private Long idQdGiaoNvNh;
	private String loaiVthh;
	private String cloaiVthh;
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
	private List<KquaKnghiemKhacReq> kquaKnghiem = new ArrayList<>();
}
