package com.tcdt.qlnvhang.request.object.khlcnt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class KhLuaChonNhaThauReq{
	private Long id;
	private String soQdinh;
	private Integer namKhoach;
	private String maVatTu;
	private String tenVatTu;
	private Long vatTuId;
	private String tenDuAn;
	private Double tongMucDtu;
	private String donViTien;
	private String dienGiai;
	private String tenChuDtu;
	private String nguonVon;
	private String hthucLcnt;
	private String tgianThienDuAn;
	private String qchuanKthuat;

	List<GoiThauReq> goiThau = new ArrayList<>();
}
