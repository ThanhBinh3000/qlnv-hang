package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ThongTinDauThauRes {
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
	private BigDecimal tongMucDtu;
	private String ghiChu;

	private Long soGoiThau;
	private Long tongGoiThau;
	private List<DTVatTuGoiThauVTRes> goiThau;
}
