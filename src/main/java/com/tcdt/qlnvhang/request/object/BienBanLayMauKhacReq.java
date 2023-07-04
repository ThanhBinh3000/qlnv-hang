package com.tcdt.qlnvhang.request.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacDtlReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanLayMauKhacReq extends BaseRequest {

	private Long id;
	private String loaiBienBan;
	private String soBienBan;
	private String donViCungCap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMau;
	private String diaDiemLayMau;
	private String diaDiemBanGiao;

	private String maVatTuCha;
	private String maVatTu;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;
	private boolean ketQuaNiemPhong;
	private String loaiVthh;

	private Integer nam;

	private String maDvi;

	private String maQhns;

	private Long idQdGiaoNvNh;
	private String soQdGiaoNvNh;

	private Long idBbGuiHang;

	private String soBbGuiHang;

	private String soHd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayQdGiaoNvNh;

	private String soBbNhapDayKho;
	private Long idBbNhapDayKho;

	private String cloaiVthh;
	private String moTaHangHoa;
	private String dviKiemNghiem;

	private String maLoKho;

	private String trangThai;

	private Long idDdiemGiaoNvNh;

	private List<BienBanLayMauKhacCtReq> chiTiets = new ArrayList<>();
	private HhQdPdNhapKhacDtlReq hhQdPdNhapKhacDtlReq;
}
