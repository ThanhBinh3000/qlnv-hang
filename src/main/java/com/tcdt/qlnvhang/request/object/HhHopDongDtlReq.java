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
public class HhHopDongDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số gói thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Cục Hà Nội")
	String shgt;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Tên gói thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên gói thầu")
	String tenGthau;

	BigDecimal soLuong;
	BigDecimal donGiaVat;
	Long vat;
	BigDecimal giaTruocVat;
	BigDecimal giaSauVat;

	String maDvi;

	String diaDiemNhap;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date tgianGiaoThucTe;
	private Integer soNgayGiaoCham;
	private BigDecimal slGiaoCham;
	private BigDecimal mucPhat;
	private BigDecimal thanhTienTinhPhat;

	private List<HhDdiemNhapKhoReq> detail;

	private List<HhDdiemNhapKhoReq> children;


}
