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
public class HhPaKhlcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String hthucLcnt;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String pthucLcnt;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiHdong;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String nguonVon;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianTbao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMthau;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Về việc không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String veViec;

	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@ApiModelProperty(notes = "Id của tổng hợp đề xuất kế hoạch lựa chọn nhà thầu")
	private Long idThHdr;

	@Size(max = 20, message = "Số phương án không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20-PA/TCDT")
	String soPhAn;

	@NotNull(message = "Không được để trống")
	String ghiChu;

	private List<HhPaKhlcntDtlReq> detail;
	private List<FileDinhKemReq> fileDinhKems;

}
