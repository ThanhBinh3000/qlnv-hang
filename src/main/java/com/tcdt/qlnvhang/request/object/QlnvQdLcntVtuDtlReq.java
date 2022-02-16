package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntVtuDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idHdr;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	String soDxuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số gói thầu không được vượt quá 50 ký tự")
	String soGoithau;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaDxKthue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaDuyetCthue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaDxCthue;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số hiếu không được vượt quá 50 ký tự")
	String soHieu;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Nguồn vốn không được vượt quá 50 ký tự")
	String nguonvon;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên gói thầu không được vượt quá 250 ký tự")
	String tenGoithau;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaDuyetKthue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongDxuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongDuyet;
	
	private List<QlnvQdLcntVtuDtlCtietReq> detail;

}
