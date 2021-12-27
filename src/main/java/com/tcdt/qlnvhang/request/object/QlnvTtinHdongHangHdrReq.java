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
public class QlnvTtinHdongHangHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số hợp đồng không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soHdong;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHetHluc;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	String soQdinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hợp đồng không được vượt quá 250 ký tự")
	String tenHdong;

	Long idTtinDthau;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Đơn vị thực hiện không được vượt quá 250 ký tự")
	String dviThien;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "MST đơn vị thực hiện không được vượt quá 20 ký tự")
	String mstDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Địa chỉ đơn vị thực hiện không được vượt quá 250 ký tự")
	String diaChi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Người đại diện không được vượt quá 250 ký tự")
	String nguoiDaiDien;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	String soDthoai;

	BigDecimal giaTriHdong;
	BigDecimal giaNoVat;
	Float vat;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Tình trạng hợp đồng không được vượt quá 2 ký tự")
	String tthaiHdong;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Tình trạng hợp đồng không được vượt quá 2 ký tự")
	String loaiHdong;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvTtinHdongHangDtl1Req> detail1;

	private List<QlnvTtinHdongHangDtl2Req> detail2;
}
