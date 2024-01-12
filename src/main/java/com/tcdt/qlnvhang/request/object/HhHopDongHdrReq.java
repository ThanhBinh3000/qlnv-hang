package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhHopDongHdrReq extends BaseRequest {

	private Long id;

	private Integer namHd;

	private String soQdKqLcnt;

	private Long idQdKqLcnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdKqLcnt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianGiaoDuHang;

	private String soQdPdKhlcnt;

	private String tenGoiThau;

	private Long idGoiThau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNkho;

	String soHd;

	String tenHd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Temporal(TemporalType.DATE)
	Date ngayKy;

	String ghiChuNgayKy;

	String loaiHdong;
	String ghiChuLoaiHdong;
	Integer soNgayThien;
	Integer tgianBhanh;

	String maDvi;

	String diaChi;
	String mst;
	String tenNguoiDdien;
	Long idNguoiDdien;
	String chucVu;
	String sdt;
	String stk;

	String tenNhaThau;
	String diaChiNhaThau;
	String mstNhaThau;
	String tenNguoiDdienNhaThau;
	String chucVuNhaThau;
	String sdtNhaThau;
	String stkNhaThau;

	String loaiVthh;

	String cloaiVthh;

	String moTaHangHoa;

	Double soLuong;
	Double donGia;

	String trangThai;


	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ghiChu;
	String namKhoach;

	String noiDung;

	String dieuKien;
	String fax;
	String moTai;
	String giayUyQuyen;
	String faxNhaThau;
	String moTaiNhaThau;
	String giayUyQuyenNhaThau;

	String donViTinh;
	Integer soNgayThienHd;
	BigDecimal soTienTinhPhat;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date tgianGiaoThucTe;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayHlucHd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date tgianBdamThienHd;

	private List<HhHopDongDtlReq> detail;

	private List<FileDinhKemReq> listFileDinhKem;
	private List<FileDinhKemReq> listCcPhapLy;

}
