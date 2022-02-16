package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idDtl;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Địa chỉ không được vượt quá 250 ký tự")
	String diaChi;
	
	@NotNull(message = "Không được để trống")
	BigDecimal soDxuat;
	
	@NotNull(message = "Không được để trống")
	BigDecimal soDuyet;
	
	@NotNull(message = "Không được để trống")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	BigDecimal donGia;
	
	@NotNull(message = "Không được để trống")
	BigDecimal thuesuat;
	
	BigDecimal thanhtien;
	BigDecimal tongtien;
}
