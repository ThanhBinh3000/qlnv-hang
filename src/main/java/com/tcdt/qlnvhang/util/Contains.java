package com.tcdt.qlnvhang.util;

import java.util.Map;

public class Contains {
	public static final String FORMAT_DATE_STR = "dd/MM/yyyy";
	public static final String NGUOI_DUNG_NOI_BO = "BE";
	public static final String NGUOI_DUNG_BEN_NGOAI = "FE";

	public static final String TAT_CA = "ALL";

	// Trang thai
	public static final String MOI_TAO = "00";
	public static final String HOAT_DONG = "01";
	public static final String NGUNG_HOAT_DONG = "02";
	public static final String TAM_KHOA = "03";

	// Trang thai bao cao
	public static final String CHO_PHE_DUYET = "00";
	public static final String CHO_KHOA_SO = "01";
	public static final String TU_CHOI_PD = "02";
	public static final String DA_KHOA = "03";
	public static final String TU_CHOI_KHOA = "04";
	public static final String MO_SO = "05";
	public static final String HUY_PDUYET = "06";

	// Trang thai phe duyet
	public static final String TAO_MOI = "00";
	public static final String CHO_DUYET = "01";
	public static final String DUYET = "02";
	public static final String TU_CHOI = "03";
	public static final String HUY = "04";
	public static final String TONG_HOP = "05";

	// Trang thai response
	public static final int RESP_SUCC = 0;
	public static final int RESP_FAIL = 1;

	public static final String TYPE_USER_BACKEND = "BE";
	public static final String TYPE_USER_FRONTEND = "FE";

	// Cap trang don vi
	public static final String CAP_TONG_CUC = "1";
	public static final String CAP_CUC = "2";
	public static final String CAP_CHI_CUC = "3";

	// Loai dieu chinh quyet dinh
	public static final String QD_GOC = "00";
	public static final String DC_GIA = "01";
	public static final String DC_SO_LUONG = "02";
	public static final String DC_THOI_GIAN = "03";
	public static final String DC_KHAC = "04";

	// Dinh nghia code gen key
	public static final String QUYET_DINH = "QD";
	public static final String QUYET_DINH_DC = "DC";
	public static final String HOP_DONG = "HD";

	// Loai mua ban
	public static final String MUA_TT = "00";
	public static final String BAN_TT = "01";

	// Ket qua chao thau
	public static final String TRUNG_THAU = "00";
	public static final String TRUOT_THAU = "01";

	// Loai hop dong
	public static final String HD_MUA = "00";
	public static final String HD_BAN = "01";

	public static final Map<String, String> mappingLoaiDc;
	static {
		mappingLoaiDc = Maps.<String, String>buildMap().put(Contains.DC_GIA, "Điều chỉnh giá")
				.put(Contains.DC_SO_LUONG, "Điều chỉnh số lượng").put(Contains.DC_THOI_GIAN, "Điều chỉnh thời gian")
				.put(Contains.DC_KHAC, "Điều chỉnh khác").get();
	}

	public static String getLoaiDc(String key) {
		return Contains.mappingLoaiDc.get(key);
	}

	public static final String TEMPLATE_SO_KHO = "/reports/SO_KHO.docx";

}
