package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhBbNghiemthuKlstHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	private Long qdgnvnxId; // Quyết định giao nhiệm vụ nhập xuất id

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số biên bản được vượt quá 20 ký tự")
	@ApiModelProperty(example = "20/BB-TCDT")
	String soBb;

	String maDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayNghiemThu;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên thủ trưởng không được vượt quá 50 ký tự")
	String thuTruong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kế toán trưởng không được vượt quá 50 ký tự")
	String keToan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kỹ thuật viên bảo quản không được vượt quá 50 ký tự")
	String kyThuatVien;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Thủ kho không được vượt quá 50 ký tự")
	String thuKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã ngăn lô được vượt quá 20 ký tự")
	@ApiModelProperty(example = "010201010101")
	String maNganlo;

	/*@Size(max = 250, message = "Ông bà được vượt quá 250 ký tự")
	String ongBa;

	@Size(max = 250, message = "Chức vụ được vượt quá 250 ký tự")
	String chucVu;*/

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã ngăn kho được vượt quá 20 ký tự")
	String maVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	@Size(max = 20, message = "Phương thức bảo quản được vượt quá 20 ký tự")
	String pthucBquan;

	@Size(max = 20, message = "Hình thức bảo quản được vượt quá 20 ký tự")
	String hthucBquan;

	Double tichLuong;
	Double slThucNhap;

	@Size(max = 20, message = "Hình thức kê lót được vượt quá 20 ký tự")
	String hthucKlot;

	@Size(max = 20, message = "Kiểu kê lót được vượt quá 20 ký tự")
	String kieuKlot;

	@Size(max = 20, message = "Loại hình kho được vượt quá 20 ký tự")
	String lhKho;

	Double dinhMuc;

	@Size(max = 20, message = "Trạng thái nhập được vượt quá 20 ký tự")
	String trangThaiNhap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKthuc;

	@Size(max = 2000, message = "Kết luận được vượt quá 2000 ký tự")
	String ketLuan;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Lý do từ chối")
	String ldoTuchoi;

	private List<HhBbNghiemthuKlstDtlReq> detail;

	private List<FileDinhKemReq> fileDinhKems;

}
