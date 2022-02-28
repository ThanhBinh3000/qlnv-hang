package com.tcdt.qlnvhang.util;

import java.util.Map;

public class Contains {
	public static final String FORMAT_DATE_STR = "dd/MM/yyyy";
	public static final String FORMAT_DATE_TIME_STR = "dd/MM/yyyy HH:mm:ss";
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
	public static final String CCUC_DUYET = "06";
	public static final String CUC_DUYET = "07";
	public static final String TCUC_DUYET = "08";
	public static final String TK_DUYET = "06";// Trang thai trung gian, thu kho phe duyet
	public static final String KTV_DUYET = "07";// Trang thai trung gian, ky thuat vien phe duyet
	public static final String KTT_DUYET = "08";// Trang thai trung gian, ke toan truong phe duyet
	public static final String TPHONG_DUYET = "09"; // Trang thai Truong phong duyet
	public static final String LANHDAO_DUYET = "10"; // Trang thai Lanh dao duyet

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
	public static final String PHIEU_NX = "PH";
	public static final String BANG_KE = "BK";
	public static final String BIEN_BAN = "BB";

	// Loai mua ban
	public static final String MUA_TT = "00";
	public static final String BAN_TT = "01";

	// Ket qua chao thau
	public static final String TRUNG_THAU = "00";
	public static final String TRUOT_THAU = "01";

	// Loai hop dong
	public static final String HD_MUA = "00";
	public static final String HD_BAN = "01";

	// Loai hop dong thong tin dau thau vat tu
	public static final String HD_VT_TRON_GOI = "00";
	public static final String HD_VT_THEO_DON_GIA = "01";
	public static final String HD_VT_THEO_DON_GIA_DIEU_CHINH = "02";
	public static final String HD_VT_THEO_THOI_GIAN = "03";

	// Loai quyet dinh
	public static final String QD_MUA = "00";
	public static final String QD_BAN = "01";

	// Loai phieu
	public static final String PHIEU_NHAP = "00";
	public static final String PHIEU_XUAT = "01";

	// Loai bang ke
	public static final String BK_NHAP = "00";
	public static final String BK_XUAT = "01";

	// Loai hinh xuat khac
	public static final String XK_CUU_TRO = "00";
	public static final String XK_VIEN_TRO = "01";
	public static final String XK_KIEM_TRA = "02";
	public static final String XK_XUAT_KHAC = "03";

	// Loai hinh xuat
	public static final String LHX_THANH_LY = "00";
	public static final String LHX_TIEU_HUY = "01";

	// Trang thai xuat
	public static final String TTHAI_XUAT_CHUA_HTHANH = "00";
	public static final String TTHAI_XUAT_HTHANH = "01";

	// Loai de xuat
	public static final String DX_THANH_LY = "00";
	public static final String DX_TIEU_HUY = "01";

	// Loai hang
	public static final String VAT_TU = "00";
	public static final String LUONG_THUC_MUOI = "01";

	// Ket qua dau thau
	public static final String DUYET_THAU = "00";
	public static final String HUY_THAU = "01";

	// Loai vat tu hang hoa
	public static final String LOAI_VTHH_GAO = "00";
	public static final String LOAI_VTHH_THOC = "01";
	public static final String LOAI_VTHH_MUOI = "02";
	public static final String LOAI_VTHH_VATTU = "03";

	public static final Map<String, String> mappingLoaiDx;
	static {
		mappingLoaiDx = Maps.<String, String>buildMap().put(Contains.DX_THANH_LY, "Thanh lý")
				.put(Contains.DX_TIEU_HUY, "Tiêu hủy").get();
	}

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

	public static final Map<String, String> mpLoaiVthh;
	static {
		mpLoaiVthh = Maps.<String, String>buildMap().put(Contains.LOAI_VTHH_GAO, "Gạo")
				.put(Contains.LOAI_VTHH_THOC, "Thóc").put(Contains.LOAI_VTHH_MUOI, "Muối")
				.put(Contains.LOAI_VTHH_THOC, "Vật tư").get();
	}

	public static String getLoaiVthh(String key) {
		return Contains.mpLoaiVthh.get(key);
	}

}
