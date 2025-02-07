package com.tcdt.qlnvhang.response.bbanlaymau;

import com.tcdt.qlnvhang.response.CommonResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class BienBanLayMauRes extends CommonResponse {
	private Long id;
	private Long qdgnvnxId;
	private String soQuyetDinhNhap;
	private Long bbNhapDayKhoId;
	private String soBbNhapDayKho;
	private Long bbGuiHangId;
	private String soBbGuiHang;
	private String soBienBan;
	private Long hopDongId;
	private String soHopDong;
	private LocalDate ngayHopDong;
	private String donViCungCap;
	private LocalDate ngayLayMau;
	private String diaDiemLayMau;
	private String diaDiemBanGiao;

	private String maVatTuCha;
	private String tenVatTuCha;
	private String maVatTu;
	private String tenVatTu;

	private String tenDiemKho;
	private String maDiemKho;
	private String tenNhaKho;
	private String maNhaKho;
	private String tenNganKho;
	private String maNganKho;
	private String tenNganLo;
	private String maNganLo;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;
	private String ketQuaNiemPhong;

	private String trangThai;
	private String tenTrangThai;
	private String trangThaiDuyet;
	private String lyDoTuChoi;
	private String maDvi;
	private String tenDvi;
	private String maQhns;
	private String loaiVthh;

	private List<BienBanLayMauCtRes> chiTiets = new ArrayList<>();
}
