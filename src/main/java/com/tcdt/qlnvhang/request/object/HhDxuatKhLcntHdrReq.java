package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	String soQd;

	@Size(max = 500, message = "Trích yếu không được vượt quá 500 ký tự")
	String trichYeu;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Chủng loại vật tư hàng hóa không được vượt quá 20 ký tự")
	String cloaiVthh;

	String moTaHangHoa;

	String maVtu;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	Long namKhoach;

	@NotNull(message = "Không được để trống")
	@Size(max = 500, message = "Tên dự án không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String tenDuAn;

	BigDecimal tongMucDt;


	//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Tiêu chuẩn chất lượng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Tiêu chuẩn")
	String tchuanCluong;

	//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nguồn vốn")
	String nguonVon;

	//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Hình thức")
	String hthucLcnt;

	//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Phương thức")
	String pthucLcnt;

	//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Loại hợp đồng")
	String loaiHdong;

	@NotNull(message = "Không được để trống")
	Integer tgianThienHd;

	//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Temporal(TemporalType.DATE)
	Date tgianNhang;

	@NotNull(message = "Không được để trống")
	Integer gtriDthau;

	@NotNull(message = "Không được để trống")
	Integer gtriHdong;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	String ghiChu;

	String dienGiai;

	String loaiHinhNx;

	String kieuNx;

	String diaChiDvi;

	private List<FileDinhKemReq> fileDinhKemReq =  new ArrayList<>();

	private List<HhDxuatKhLcntDsgtDtlReq> dsGtReq =  new ArrayList<>();

	private List<HhDxuatKhLcntCcxdgDtlReq> ccXdgReq =  new ArrayList<>();

}
