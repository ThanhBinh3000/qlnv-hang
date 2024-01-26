package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdGiaoNvuNhapxuatHdrReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@Size(max = 20, message = "Số quyết định được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@ApiModelProperty(example = "2022-09-05")
	Date ngayQdinh;

	String maDvi;

	@Size(max = 2, message = "Loại quyết định được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiQd;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
	String ghiChu;

	String loaiVthh;

	String cloaiVthh;

	String moTaHangHoa;

	String trichYeu;

	Integer namNhap;

	Long idHd;

	String soHd;

	String tenGoiThau;

	String donViTinh;
	String dviCungCap;

	Long soLuong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNkho;

	private List<HhQdGiaoNvuNhapxuatDtlReq> detailList;

	private List<FileDinhKemReq> fileDinhKems;
	private List<FileDinhKemReq> fileCanCu;

}
