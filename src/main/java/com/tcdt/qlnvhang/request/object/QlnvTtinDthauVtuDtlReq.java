package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauVtuDtlReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idHdr;
	String tenGoiThau;
	String soHieu;
	BigDecimal soLuong;
	BigDecimal giaThau;
	String hthucLcnt;
	String pthucLcnt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date tuNgayLcnt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date denNgayLcnt;
	String loaiHdong;
	BigDecimal tgianHdong;
	String tenPage;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date ngayDangPage;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date tuNgayPhanhHsmt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	Date denNgayPhanhHsmt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	Date ngayMothau;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	Date ngayDongThau;
	
	private List<QlnvTtinDthauVtuDtl1Req> dtl1List;
	private List<QlnvTtinDthauVtuDtl2Req> dtl2List;
}
