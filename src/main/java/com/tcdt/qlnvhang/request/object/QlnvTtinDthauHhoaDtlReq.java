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
public class QlnvTtinDthauHhoaDtlReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	Long idHdr;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên gói thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "TGT123")
	String tenGoiThau;
	
	@Size(max = 50, message = "Số hiệu không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SH123")
	String soHieu;
	
	@NotNull(message = "Không được để trống")
	BigDecimal soLuong;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaThau;
	
	@Size(max = 2, message = "Hình thức lựa chọn nhà thầu không được vượt quá 2 ký tự")
	String hthucLcnt;
	
	@Size(max = 2, message = "Phương thức lựa chọn nhà thầu không được vượt quá 2 ký tự")
	String pthucLcnt;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayLcnt;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayLcnt;
	
	@Size(max = 2, message = "Loại hợp đồng không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")

	String loaiHdong;
	
	@NotNull(message = "Không được để trống")
	BigDecimal tgianHdong;
	
	@Size(max = 250, message = "Tên page không được vượt quá 250 ký tự")
	String tenPage;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDangPage;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayPhanhHsmt;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayPhanhHsmt;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayMothau;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDongthau;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MDV1")
	String maDvi;
	
	private List<QlnvTtinDthauHhoaDtl1Req> dtl1List;
}
