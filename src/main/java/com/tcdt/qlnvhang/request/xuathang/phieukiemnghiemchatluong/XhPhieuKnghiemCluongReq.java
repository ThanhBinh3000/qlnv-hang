package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhPhieuKnghiemCluongReq extends BaseRequest {
	private Long id;

	private Integer nam;

	private String maDvi;

	private String maQhns;

	private String soBbLayMau;

	private String soQdGiaoNvXh;

	private Long idQdGiaoNvXh;

	private Long idKtv;

	private String soPhieu;

	private Long idDdiemXh;

	private String maDiemKho;

	private String maNhaKho;

	private String maNganKho;

	private String maLoKho;

	private String loaiVthh;

	private String cloaiVthh;

	private String moTaHangHoa;

	private String hthucBquan;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayKnghiem;

	private String ketQua;

	private String ketLuan;

	@Transient
	private List<XhPhieuKnghiemCluongCt> children = new ArrayList<>();
}
