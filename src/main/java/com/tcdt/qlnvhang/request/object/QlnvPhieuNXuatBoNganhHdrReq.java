package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvPhieuNXuatBoNganhHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	String soPhieu;
	Date ngayLap;
	String soQdinhNhapxuat;
	String maKho;
	String maNgan;
	String maLo;
	String maHhoa;
	String tenHhoa;
	String maDvi;
	String diaChi;
	String lhinhNhapxuat;
	String tkhoanCo;
	String tkhoanNo;
	Date ngayNhan;
	String tenNguoiNhan;
	String loaiCtu;
	String soPhieuKtcl;
	String loaiNhapxuat;
	String trangThai;
	private List<QlnvPhieuNXuatBoNganhDtlReq> detailListReq;
}
