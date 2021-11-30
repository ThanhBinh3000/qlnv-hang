package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvKhoachLcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại hàng hóa không được vượt quá 2 ký tự")
	String loaiHanghoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	String soQdGiaoCtkh;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số kế hoạch không được vượt quá 20 ký tự")
	String soKhDtoan;

	@NotNull(message = "Không được để trống")
	Date ngayQd;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số văn bản không được vượt quá 20 ký tự")
	String soVban;

	@NotNull(message = "Không được để trống")
	Date ngayVban;
	String ldoTuchoi;
	@NotNull(message = "Không được để trống")
	String maDvi;
	@NotNull(message = "Không được để trống")
	String nguonvon;
	@NotNull(message = "Không được để trống")
	String hanghoa;
	
	@NotNull(message = "Không được để trống")
	String soDx;
	
	@NotNull(message = "Không được để trống")
	String ngayDx;
	@NotNull(message = "Không được để trống")
	String tenGoiThau;
	@NotNull(message = "Không được để trống")
	BigDecimal tongTien;
	@NotNull(message = "Không được để trống")
	Integer soLuong;
	@NotNull(message = "Không được để trống")
	String donVi;//don vi tinh
	Date ngayThienTu;
	Date ngayThienDen;
	String tcChatLuong;
	@NotNull(message = "Không được để trống")
	@Length(min = 0, max = 999)
	Integer soPhanThau;
	@NotNull(message = "Không được để trống")
	String hthucLcnt;
	@NotNull(message = "Không được để trống")
	String pthucLcnt;
	Date ngayPhanh;
	Date thoiHanNhap;
	BigDecimal giaBan;
	BigDecimal tienBaoLanh;
	BigDecimal tienDamBao;
	Date ngayDongThau;
	Date ngayMoHso;
	String loaiHdong;
	BigDecimal giaTtinh;
	String kienNghi;
	
	private List<QlnvKhoachLcntDtlReq> detailList;
	
	
	
	
}
