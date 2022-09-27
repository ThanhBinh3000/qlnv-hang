package com.tcdt.qlnvhang.request.object.bbanlaymau;

import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanBanGiaoMauReq extends SoBienBanPhieuReq {
	private Long id;

	private Long idQdGiaoNvNx;
	private String soQdGiaoNvNx;
	private String soBbNhapDayKho;
	private Long idBbNhapDayKho;
	private Integer nam;

	private String soHd;
	private Date ngayQdGiaoNvNx;
	private String loaiVthh;
	private String cloaiVthh;
	private Long bbNhapDayKhoId;
	private String soBienBan;
	private Date ngayLayMau;
	private String dviKiemNghiem;
	private String diaDiemLayMau;

	private String maDiemKho;
	private String maNhaKho;
	private String maNganKho;
	private String maNganLo;

	private Integer soLuongMau;
	private String ppLayMau;
	private String chiTieuKiemTra;
	private Boolean ketQuaNiemPhong;

	private String trangThai;
	private String maDvi;
	private List<BienBanLayMauCtReq> chiTiets = new ArrayList<>();
}
