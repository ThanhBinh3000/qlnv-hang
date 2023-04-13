package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import lombok.Data;

@Data
public class XhPhieuKnghiemCluongCtReq {

	private Long idHdr;

	private String tenTchuan;

	private String ketQuaKiemTra; // Ket qua phan tich

	private String phuongPhap;

	private String trangThai;

	private String chiSoNhap;

	private String kieu;

	private String danhGia;
}
