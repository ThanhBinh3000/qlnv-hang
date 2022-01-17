package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvKhoachLcntDtlReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idHdr;
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	String maDvi;
	@NotNull(message = "Không được để trống")
	String phanThau;
	@NotNull(message = "Không được để trống")
	BigDecimal soLuong;
	@NotNull(message = "Không được để trống")
	String dviTinh;
	BigDecimal giaDkien;
	BigDecimal giaDkienVat;
	String diaChi;
	String maDkho;
}
