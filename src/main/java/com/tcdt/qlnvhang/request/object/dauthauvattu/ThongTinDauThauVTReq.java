package com.tcdt.qlnvhang.request.object.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ThongTinDauThauVTReq {
	private Long id;
	private Long qdKhlcntId;
	private String soDxuat;
	private LocalDate ngayDxuat;
	private String soQdinhPduyetKhlcnt;
	private String ngayPduyetKhlcnt;
	private String tenDuAn;
	private String tenChuDtu;
	private String benMoiThau;
	private String hthucDuThau;
	private LocalDate tgianDongThau;
	private Integer tgianHlucEhsmt;
	private Double tongMucDtu;
	private String ghiChu;
	List<DTVatTuGoiThauVTReq> goiThau = new ArrayList<>();
}
