package com.tcdt.qlnvhang.request.object.khlcnt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GoiThauReq {
	private Long id;
	private Integer stt;
	private Long khLcntId;
	private String tenGoiThau;
	private String maVatTu;
	private String tenVatTu;
	private Long vatTuId;
	private Double soLuong;
	private Double donGia;
	private List<DiaDiemNhapReq> diaDiemNhap = new ArrayList<>();
}
