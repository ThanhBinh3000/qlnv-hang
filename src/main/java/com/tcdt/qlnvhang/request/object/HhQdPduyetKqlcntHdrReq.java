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
public class HhQdPduyetKqlcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	String cloaiVthh;

	@Size(max = 500, message = "Về việc không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String trichYeu;

	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@NotNull(message = "Không được để trống")
	@Size(max = 500, message = "Căn cứ quyết định phê duyệt kế hoạch lựa chọn nhà thầu không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "20/QD-TCDT")
	String soQdPdKhlcnt;

//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "0102")
	String maDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdPdKhlcnt;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "00")
	String ghiChu;

	Long idGoiThau;

	Integer trungThau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	String lyDoHuy;

	private List<HhQdPduyetKqlcntDtlReq> detailList;

	private List<FileDinhKemReq> fileDinhKems;

}
