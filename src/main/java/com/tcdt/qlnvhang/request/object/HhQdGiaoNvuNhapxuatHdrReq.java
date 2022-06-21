package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdGiaoNvuNhapxuatHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@Size(max = 20, message = "Số quyết định được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQd;

	@Size(max = 250, message = "Về việc không được vượt quá 250 ký tự")
	String veViec;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@ApiModelProperty(example = "2022-09-05")
	Date ngayKy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@ApiModelProperty(example = "2022-09-05")
	Date ngayQdinh;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	@Size(max = 20, message = "Số hợp đồng được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/HD-TCDT")
	String soHd;

	String maDvi;

	@Size(max = 2, message = "Loại quyết định được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiQd;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
	String ghiChu;

	/**
	 * @link QdGiaoNvNhapXuatLoaiHangHoaEnum
	 */
	String loaiVthh;

	String trichYeu;

	Integer namNhap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgNhapKhoMuonNhat;

	private List<HhQdGiaoNvuNhapxuatDtlReq> detail;

	/*private List<HhDviThuhienQdinhReq> detail1;*/

	private List<FileDinhKemReq> fileDinhKems;

}
