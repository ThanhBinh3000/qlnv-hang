package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhBbNghiemthuKlstDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	private Long hdrId;
	private String danhMuc;
	private String nhomHang;
	private String donViTinh;
	private String matHang;
	private String tenMatHang;
	private String donViTinhMh;
	private Double tongGiaTri;
	private Double soLuongTrongNam;
	private Double donGia;
	private Double thanhTienTrongNam;
	private Double soLuongNamTruoc;
	private Double thanhTienNamTruoc;
	private String type;
	private Boolean isParent;
	private String idParent;

}
