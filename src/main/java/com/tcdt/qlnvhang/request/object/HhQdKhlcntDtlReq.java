package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdKhlcntDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Cục Hà Nội")
	String tenDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số đề xuất không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String soDxuat;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayTao;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên dự án không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String tenDuAn;

	BigDecimal soLuong;
	BigDecimal donGiaVat;
	BigDecimal donGiaTamTinh;
	BigDecimal tongTien;
	Long soGthau;
	String tchuanCluong;

	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	Long idDxHdr;

	String diaChiDvi;
	String trichYeu;

	String goiThau;

	String cloaiVthh;

	String loaiVthh;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianBdauTchuc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthauTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthauTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMoHoSo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMoHoSoTime;
	BigDecimal giaBanHoSo;
	Long idHhQdKhlcntDtl;

	private List<HhQdKhlcntDsgthauReq> dsGoiThau = new ArrayList<>();

	private List<HhQdKhlcntDsgthauReq> children = new ArrayList<>();

}
