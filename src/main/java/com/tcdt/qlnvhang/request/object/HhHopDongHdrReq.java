package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhHopDongHdrReq {

	private Long id;

	private Integer namHd;

	private String soQdKqLcnt;

	private Long idQdKqLcnt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdKqLcnt;

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
	String idNguoiDdien;
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
//
//
//
//	@ApiModelProperty(notes = "Bắt buộc set đối với update")
//	private Long id;
//
//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Số hợp đồng được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "20/HD-TCDT")
//	String soHd;
//
//	@NotNull(message = "Không được để trống")
//	@Size(max = 250, message = "Tên hợp đồng được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "Tên hợp đồng")
//	String tenHd;
//
//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Số quyết định phê duyệt kết quả lcnt được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "20/QD-TCDT")
//	String canCu;
//
//	Long canCuId;
//
//	String idGoiThau;
//
////	String dviTrungThau;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date tuNgayHluc;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date denNgayHluc;
//
//	Double soNgayThien;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date tuNgayTdo;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date denNgayTdo;
//
//	Double soNgayTdo;
//
////	@NotNull(message = "Không được để trống")
////	@Size(max = 20, message = "Nước sản xuất được vượt quá 20 ký tự")
////	@ApiModelProperty(example = "Nhật Bản")
//	String nuocSxuat;
//
////	@NotNull(message = "Không được để trống")
////	@Size(max = 20, message = "Tiêu chuẩn chất lượng được vượt quá 20 ký tự")
////	@ApiModelProperty(example = "Tiêu chuẩn")
//	String tieuChuanCl;
//
//	Double soLuong;
//
//	Double donGiaVat;
//
//	Double gtriHdTrcVat;
//	Double vat;
//	Double gtriHdSauVat;
//
//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "00")
//	String loaiVthh;
//
//	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
//	@ApiModelProperty(example = "00")
//	String cloaiVthh;
//	String moTaHangHoa;
//
////	@NotNull(message = "Không được để trống")
////	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
////	@ApiModelProperty(example = "00")
//	String loaiHd;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date ngayKy;
//
//	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
//	@ApiModelProperty(example = "Lý do từ chối")
//	String ldoTuchoi;
//
//	@Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
//	@ApiModelProperty(example = "Ghi chú")
//	String ghiChu;
//
//	@NotNull(message = "Không được để trống")
//	@ApiModelProperty(example = "2022")
//	Long namHd;
//
//	Integer tgianBhanh;
//
//	//Thông tin chủ đầu tư
//
//	String maDvi;
//
//	String diaChi;
//
//	String mst;
//
//	String sdt;
//
//	String stk;
//
//	String tenNguoiDdien;
//
//	String chucVu;
//
//	String idNthau;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
//	Date tgianNkho;

	private List<HhHopDongDtlReq> detail;

	private List<FileDinhKemReq> listFileDinhKem;
	private List<FileDinhKemReq> listCcPhapLy;

}
