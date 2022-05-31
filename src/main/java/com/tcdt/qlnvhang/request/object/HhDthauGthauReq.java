package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDthauGthauReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 250, message = "Tên gói thầu không được vượt quá 250 ký tự")
//	@ApiModelProperty(example = "Tên gói thầu")
	String tenGthau;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Mã hàng hóa không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "HH001")
	String maHhoa;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Đơn vị tính không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "DVT01")
	String dviTinh;

	BigDecimal soLuong;
	BigDecimal giaGthau;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "NGV01")
	String nguonVon;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "HLC01")
	String hthucLcnt;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "HLC01")
	String pthucLcnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuTgianLcnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denTgianLcnt;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Hình thức hợp đồng không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "HTH01")
	String hthucHdong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianThHienHd;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMoHsdxtc;

//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "20-QĐ/VPH")
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	private Long idDtHdr;

	private Long idGoiThau;

	private String nhaThauTthao;

	private Long donGia;

	private List<HhDthauNthauDuthauReq> children;

	private List<HhDthauHsoKthuatReq> detail1;

	private List<HhDthauHsoTchinhReq> detail2;

	private List<HhDthauTthaoHdongReq> detail3;

	private List<HhDthauKquaLcntReq> detail4;

	private List<FileDinhKemReq> fileDinhKems;

}
