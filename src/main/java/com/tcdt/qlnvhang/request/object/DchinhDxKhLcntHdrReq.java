package com.tcdt.qlnvhang.request.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DchinhDxKhLcntHdrReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	private Integer nam;

	String soQdDc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	String soQdGoc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdGoc;

	String idQdGoc;

	String trichYeu;

	String loaiVthh;

	String cloaiVthh;

	String moTaHangHoa;

	String hthucLcnt;

	String pthucLcnt;

	String loaiHdong;

	String nguonVon;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@Size(max = 50, message = "Lý do từ chối không được vượt quá 50 ký tự")
	String ldoTuchoi;
	
	String loaiQd;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;
	
//	@NotNull(message = "Không được để trống")
	String soQdGiaoCtkh;
	
	@Size(max = 2, message = "Lý do từ chối không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String loaiDieuChinh;

	private List<FileDinhKemReq> fileDinhKem =  new ArrayList<>();

	private List<DchinhDxKhLcntDtlReq> detail;

	//	@NotNull(message = "Không được để trống")

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	//	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Về việc không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nội dung về việc")
	String veViec;

	//	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;


	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

	//	@Size(max = 200, message = "Bảo lãnh dự thầu không được vượt quá 200 ký tự")
	@ApiModelProperty(example = "Số tiền bảo lãnh dự thầu")
	String blanhDthau;

	String maTrHdr;

	private List<HhQdKhlcntDtlReq> children = new ArrayList<>();

	private List<FileDinhKemReq> fileDinhKems;
}
