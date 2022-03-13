package com.tcdt.qlnvhang.response.khlcnt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GoiThauRes {
	private Long id;
	private Long khLcntId;
	private String tenGoiThau;
	private String maVatTu;
	private String tenVatTu;
	private Long vatTuId;
	private Double soLuong;
	private Double donGia;
	private Integer stt;
	private List<DiaDiemNhapRes> diaDiemNhap = new ArrayList<>();

	public Double getThanhTien() {
		if (soLuong != null && donGia != null)
			return soLuong * donGia;

		return 0D;
	}
}
