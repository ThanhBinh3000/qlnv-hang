package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdPduyetKqlcntDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	Long idGt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số hiệu gói thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "01/SHGT-TCDT")
	String shgt;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên gói thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên gói thầu")
	String tenGthau;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Địa điểm nhập hàng không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên địa điểm nhập hàng")
	String diaDiem;

	BigDecimal soLuong;
	BigDecimal giaGthau;

//	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Cục Hà Nội")
	String tenDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "LHD01")
	String loaiHd;

	BigDecimal donGia;
	BigDecimal vat;
	BigDecimal dgiaSauVat;
	BigDecimal donGiaHd;
	BigDecimal vatHd;

	String loaiVthh;
	String cloaiVthh;
	Long idNhaThau;
	Integer trungThau;
	String lyDoHuy;
	Integer tgianThienHd;
	BigDecimal donGiaTrcVat;
}
