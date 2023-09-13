package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhBbNghiemthuKlstHdrReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	private Long idQdGiaoNvNh; // Quyết định giao nhiệm vụ nhập xuất id

	@ApiModelProperty(example = "20/BB-TCDT")
	String soBbNtBq;

	String maDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayNghiemThu;

	@Size(max = 20, message = "Mã ngăn lô được vượt quá 20 ký tự")
	@ApiModelProperty(example = "010201010101")
	String maLoKho;
	String maDiemKho;
	String maNhaKho;
	String maNganKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;
	String cloaiVthh;
	String moTaHangHoa;

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

	Integer hopDongId;

	Long idDdiemGiaoNvNh;

	String soQdGiaoNvNh;

	String soPhieuNhapKho;
	String lhinhBquan;


	private List<HhBbNghiemthuKlstDtlReq> detail;

	private List<FileDinhKemReq> fileDinhKems;

	private ReportTemplateRequest reportTemplateRequest;

}
