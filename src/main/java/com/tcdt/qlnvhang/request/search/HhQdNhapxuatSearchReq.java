package com.tcdt.qlnvhang.request.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhQdNhapxuatSearchReq extends BaseRequest {
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayQd;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayLP;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayLP;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayGD;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayGD;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayKT;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayKT;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date ngayLayMauTu;
	String ngayLayMauTuStr;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date ngayLayMauDen;
	String ngayLayMauDenStr;

	String maDvi;

	String maVthh;

	String loaiQd;
	String soBbNtBq;
	String kqDanhGia;

	Long namNhap;

	String loaiVthh;
	String cloaiVthh;

	String trichYeu;
	String soBienBan;
	String dviKiemNghiem;
	Long idQdGiaoNvNh;
	Long idDdiemGiaoNvNh;
	String maDviDtl;

	List<String> bienBan = new ArrayList<>();

	//Search bb chuan bi kho
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayTao;
	String tuNgayTaoStr;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayTao;
	String denNgayTaoStr;
	String soBbCbk;
	String soPhieuKncl;
	String soBbBanGiao;
	String soBbNhapDayKho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayKncl;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayKncl;

	String tuNgayNhapDayKhoStr;
	String denNgayNhapDayKhoStr;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayNhapDayKho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayNhapDayKho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayTgianNkho;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayTgianNkho;
}
