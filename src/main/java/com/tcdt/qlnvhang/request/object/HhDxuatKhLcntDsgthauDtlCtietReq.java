package com.tcdt.qlnvhang.request.object;

import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhLcntDsgthauDtlCtietReq {
//	@NotNull(message = "Không được để trống")
	Long id;

	BigDecimal soLuong;
	
//	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String tenDvi;
	
//	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;

	String maDiemKho;

	BigDecimal donGia;
	BigDecimal donGiaLastest;
	BigDecimal donGiaTamTinh;

	String diaDiemNhap;

	BigDecimal soLuongTheoChiTieu;

	BigDecimal soLuongDaMua;

	List<HhDxuatKhLcntDsgthauDtlCtietVtReq> children = new ArrayList<>();

}
