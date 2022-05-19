package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDxuatKhLcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "01/DX-TCDT")
	String soDxuat;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Căn cứ quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "01/QD-TCDT")
	String qdCanCu;

	@Size(max = 500, message = "Trích yếu không được vượt quá 500 ký tự")
	String trichYeu;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	Long namKhoach;

	@Size(max = 2000, message = "Mã đơn vị không được vượt quá 2000 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

	private List<FileDinhKemReq> fileDinhKems;

	private HhDxuatKhLcntGaoDtlReq detail1;

	private List<HhDxuatKhLcntDsgtDtlReq> detail2;

	private List<HhDxuatKhLcntCcxdgDtlReq> detail3;
}
