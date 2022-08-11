package com.tcdt.qlnvhang.util;

import java.math.BigDecimal;
import java.util.Map;

public class Contains {
	public static final String FORMAT_DATE_STR = "yyyy-MM-dd";
	public static final String FORMAT_DATE_TIME_STR = "yyyy-MM-dd HH:mm";
	public static final String NGUOI_DUNG_NOI_BO = "BE";
	public static final String NGUOI_DUNG_BEN_NGOAI = "FE";

	public static final String TAT_CA = "ALL";
	//Nhập vật tư
	public static final String DU_THAO = "00";
	public static final String CHO_DUYET_TP = "01";
	public static final String CHO_DUYET_LD_C = "02";
	public static final String DA_DUYET = "03";
	public static final String TU_CHOI_TP = "04";
	public static final String TU_CHOI_LD_C = "05";

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

	// Trang thai tờ tr
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

	public static final String BAN_HANH = "11"; // Trang thai Lanh dao duyet
	public static final String LANHDAO_TU_CHOI = "12";
	public static final String TT_DA_QUYET_DINH = "13"; // Trang thai Lanh dao duyet

	// Trạng thái tổng hợp
	public static final String CHUA_QUYET_DINH = "00";

	public static final String DU_THAO_QD = "01";
	public static final String DA_QUYET_DINH = "02";

	// Trạng thái gói thầu

	public static final String CHUA_CAP_NHAT = "00";

	public static final String DANG_CAP_NHAT = "01";

	public static final String HOAN_THANH_CAP_NHAT = "02";

	public static final String GT_TRUNG_THAU = "03";

	public static final String GT_HUY_THAU = "04";

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
	public static final String SHGT = "SHGT";
	public static final String PHU_LUC = "PL";

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
	public static final String QD_NHAP = "00";
	public static final String QD_XUAT = "01";

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
	@Deprecated
	public static final String VAT_TU = "00";
	@Deprecated
	public static final String LUONG_THUC_MUOI = "01";

	// Ket qua dau thau
	public static final String DUYET_THAU = "00";
	public static final String HUY_THAU = "01";

	// Loai vat tu hang hoa
	public static final String LOAI_VTHH_GAO = "0102";
	public static final String LOAI_VTHH_THOC = "0101";
	public static final String LOAI_VTHH_MUOI = "04";
	public static final String LOAI_VTHH_VATTU = "02";

	// Don vi tinh
	public static final String DVT_KG = "kg";
	public static final String DVT_YEN = "yen";
	public static final String DVT_TA = "ta";
	public static final String DVT_TAN = "tan";
	public static final String COLUMN_CONVERT = "SoLuong";

	// Danh muc
	public static final String DM_NGUON_VON = "NGUON_VON";
	public static final String DM_LOAI_HD = "LOAI_HDONG";
	public static final String DM_HT_LCNT = "HT_LCNT";
	public static final String DM_PTHUC_DTHAU = "PT_DTHAU";

	// Flag status
	public static final String ACTIVE = "Y";
	public static final String DISABLE = "N";

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
				.put(Contains.LOAI_VTHH_VATTU, "Vật tư").get();
	}

	public static String getLoaiVthh(String key) {
		return Contains.mpLoaiVthh.get(key);
	}

	public static final Map<String, BigDecimal> mpDVTinh;
	static {
		mpDVTinh = Maps.<String, BigDecimal>buildMap().put(Contains.DVT_KG, new BigDecimal(1))
				.put(Contains.DVT_YEN, new BigDecimal(10)).put(Contains.DVT_TA, new BigDecimal(100))
				.put(Contains.DVT_TAN, new BigDecimal(1000)).get();
	}

	public static BigDecimal getDVTinh(String key) {
		return Contains.mpDVTinh.get(key);
	}

	public static final Map<String, String> mapTrangThaiPheDuyet;
	static {
		mapTrangThaiPheDuyet = Maps.<String, String>buildMap()
				.put(Contains.MOI_TAO, "Mới tạo")
				.put(Contains.CHO_DUYET, "Chờ duyệt")
				.put(Contains.DUYET, "Đã duyệt")
				.put(Contains.TU_CHOI, "Từ chối")
				.put(Contains.HUY, "Hủy")
				.put(Contains.TONG_HOP, "Tổng hợp")
				.put(Contains.CCUC_DUYET, "Chi cục duyệt")
				.put(Contains.CUC_DUYET, "Cục duyệt")
				.put(Contains.TCUC_DUYET, "Tổng cục duyệt")
				.put(Contains.TK_DUYET, "Thủ kho duyệt")
				.put(Contains.KTV_DUYET, "Kỹ thuật viên duyệt")
				.put(Contains.KTT_DUYET, "Kế toán trưởng duyệt")
				.put(Contains.TPHONG_DUYET, "Trưởng phòng duyệt")
				.put(Contains.LANHDAO_DUYET, "Lãnh đạo duyệt")
				.get();
	}
}
