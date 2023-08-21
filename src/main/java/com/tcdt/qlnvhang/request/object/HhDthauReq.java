package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDthauReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	private Long idGoiThau;

	private String loaiVthh;

	private String trangThai;
	private String type;
	private BigDecimal donGia;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Số hợp đồng không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "20-HĐ/VPH")
//	String soHd;
//
////	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Tên hợp đồng không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "Tên hợp đồng")
//	String tenHd;
//
////	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "20-QĐ/VPH")
//	String soQd;
//
////	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "00")
//	String loaiVthh;
//
////	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "0102")
//	String maDvi;
//
////	@NotNull(message = "Không được để trống")
//	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
//	@ApiModelProperty(example = "2022")
//	String namKhoach;
//
////	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
//	@ApiModelProperty(example = "Lý do từ chối")
//	String ldoTuchoi;
//
////	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
//	@ApiModelProperty(example = "Ghi chú")
//	String ghiChu;
//
//	private List<HhDthauGthauReq> detail;

	private String ghiChuTtdt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date tgianTrinhKqTcg;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date tgianTrinhTtd;
	private List<FileDinhKemReq> fileDinhKems;
	private List<HhDthauNthauDuthauReq> nthauDuThauList = new ArrayList<>();

}
