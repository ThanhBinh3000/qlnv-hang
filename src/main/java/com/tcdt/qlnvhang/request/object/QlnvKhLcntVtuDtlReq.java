package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	BigDecimal giaKthue;
	BigDecimal tienCthue;
	BigDecimal giaCthue;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String soHieu;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String maDvi;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String nguonvon;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 250 ký tự")
	String tenGoithau;
	
	Integer tgianHdong;
	BigDecimal tienKthue;
	Integer soLuong;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String dviTinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLcnt;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String hthucLcnt;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String pthucLcnt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDongThau;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayMoHso;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String loaiHdong;
	
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 250 ký tự")
	String luuY;
	private List<QlnvKhLcntVtuDtlCtietReq> detailList;
}
