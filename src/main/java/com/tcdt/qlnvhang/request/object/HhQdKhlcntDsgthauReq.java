package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdKhlcntDsgthauReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 200, message = "Tên gói thầu không được vượt quá 200 ký tự")
	@ApiModelProperty(example = "Tên gói thầu")
	String goiThau;

	BigDecimal soLuong;

	String maDvi;

	BigDecimal donGiaVat;
	BigDecimal donGiaTamTinh;
	BigDecimal thanhTien;

	String loaiVthh;
	String cloaiVthh;
	String dviTinh;

	@Size(max = 50, message = "Hình thức lựa chọn nhà thầu không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String hthucLcnt;

	@Size(max = 50, message = "Phương thức lựa chọn nhà thầu Tên gói thầu không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String pthucLcnt;

	@Size(max = 50, message = "Loại hợp đồng không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String loaiHdong;

	@Size(max = 50, message = "Nguồn vốn không được vượt quá 50 ký tự")
	@NotNull(message = "Không được để trống")
	String nguonVon;

	String tgianBdauLcnt;

	@NotNull(message = "Không được để trống")
	Integer tgianThienHd;

	String diaDiemNhap;

	String tgianBdauThien;
	BigDecimal soLuongTheoChiTieu;
	BigDecimal soLuongDaMua;

	private List<HhDxuatKhLcntDsgthauDtlCtietReq> children = new ArrayList<>();

}
