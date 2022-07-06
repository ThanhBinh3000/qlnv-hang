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
public class HhDthauGthauReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	private Long idGoiThau;

	private String soQdPdKhlcnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdPdKhlcnt;

	String tenGthau;

	String loaiVthh;

	String cloaiVthh;

	String dviTinh;

	Long donGia;

	BigDecimal soLuong;

	BigDecimal tongTien;

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

	String loaiHdong;

	Integer tgianThienHd;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNhang;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianDthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKyBban;

	private Long idDtHdr;

	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Ghi chú")
	String ghiChu;

	private Long idNhaThau;

	private BigDecimal donGiaTrcVat;

	private Long vat;

	private List<HhDthauNthauDuthauReq> nthauDuThauList = new ArrayList<>();

	private List<HhDxuatKhLcntDsgthauDtlCtietReq> diaDiemNhap = new ArrayList<>();

//	private List<HhDthauHsoKthuatReq> hsoKthuatList;
//
//	private List<HhDthauHsoTchinhReq> hsoTchinhLinh;

//	private HhDthauTthaoHdongReq tthaoHdong;

//	private HhDthauKquaLcntReq kquaLcnt;

	private String trangThaiLuu;


}
