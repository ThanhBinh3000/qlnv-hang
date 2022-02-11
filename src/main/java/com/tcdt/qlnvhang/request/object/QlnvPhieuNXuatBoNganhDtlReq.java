package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvPhieuNXuatBoNganhDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	@NotNull(message = "Mã cha không được để trống")
	Long idHdr;
	String maHhoa;	
	BigDecimal soLuongCtu;
	String dviTinh;
	BigDecimal soLuongThucte;
	BigDecimal donGia;
	BigDecimal tongTien;
}
