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
public class HhQdKhlcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	String cloaiVthh;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String hthucLcnt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String pthucLcnt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiHdong;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String nguonVon;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianPhanh;

//	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Về việc không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String veViec;

//	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Căn cứ quyết định giao chỉ tiêu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20-QD/TCDT")
	String soQdCc;

	@ApiModelProperty(notes = "Id của phương án đề xuất kế hoạch lựa chọn nhà thầu (nếu có)")
	private Long idPaHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20-QD/TCDT")
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

//	@Size(max = 200, message = "Bảo lãnh dự thầu không được vượt quá 200 ký tự")
	@ApiModelProperty(example = "Số tiền bảo lãnh dự thầu")
	String blanhDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	String trichYeu;

//	@NotNull(message = "Không được để trống")
	private Long idThHdr;
	private Long idTrHdr;

	private List<HhQdKhlcntDtlReq> dsDeXuat;
	private List<FileDinhKemReq> fileDinhKems;
	private List<HhQdKhlcntDsgthauReq> dsGoiThau;

}
