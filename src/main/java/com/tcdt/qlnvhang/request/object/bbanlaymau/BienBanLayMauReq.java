package com.tcdt.qlnvhang.request.object.bbanlaymau;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMauCt;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BienBanLayMauReq extends BaseRequest {

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

	private List<BienBanLayMauCtReq> chiTiets = new ArrayList<>();
}
