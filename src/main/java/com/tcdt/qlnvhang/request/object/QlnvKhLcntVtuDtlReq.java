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
public class QlnvKhLcntVtuDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	Long khLcntVtuId;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaKthue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal tienCthue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaCthue;
	
	@Size(max = 50, message = "Số hiệu không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String soHieu;
	
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String maDvi;
	
	@Size(max = 50, message = "Nguồn vốn không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String nguonvon;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên gói thầu không được vượt quá 250 ký tự")
	String tenGoithau;
	
	@NotNull(message = "Không được để trống")
	Integer tgianHdong;
	
	@NotNull(message = "Không được để trống")
	BigDecimal tienKthue;
	
	@NotNull(message = "Không được để trống")
	Integer soLuong;
	
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String dviTinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLcnt;
	
	@Size(max = 50, message = "Hình thức lựa chọn nhà thầu không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String hthucLcnt;
	
	@Size(max = 50, message = "Phương thức lựa chọn nhà thầu Tên gói thầu không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String pthucLcnt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDongThau;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayMoHso;
	
	@Size(max = 50, message = "Loại hợp đồng không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String loaiHdong;
	
	@Size(max = 250, message = "Lưu ý không được vượt quá 250 ký tự")
	String luuY;
	private List<QlnvKhLcntVtuDtlCtietReq> detailList;
}
