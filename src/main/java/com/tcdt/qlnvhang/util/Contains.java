package com.tcdt.qlnvhang.util;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Contains {
  public static final String FORMAT_DATE_STR = "yyyy-MM-dd";
  public static final String FORMAT_DATE = "dd/MM/yyyy";
  public static final String FORMAT_DATE_TIME_STR = "yyyy-MM-dd HH:mm";
  public static final String FORMAT_DATE_TIME_FULL_STR = "yyyy-MM-dd HH:mm:ss";
  public static final String NGUOI_DUNG_NOI_BO = "BE";
  public static final String NGUOI_DUNG_BEN_NGOAI = "FE";

  public static final String TAT_CA = "ALL";
  //Nhập vật tư
  public static final String DU_THAO = "00";
  public static final String CHO_DUYET_TP = "01";
  public static final String TU_CHOI_TP = "02";
  public static final String CHO_DUYET_LDC = "03";
  public static final String TU_CHOI_LDC = "04";

  public static final String DA_DUYET_LDC = "05";
  public static final String DA_DUYET = "03";

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


  //Trạng thái dùng chung
  public static final String DUTHAO = "00";
  public static final String CHODUYET_TP = "01";
  public static final String TUCHOI_TP = "02";
  public static final String CHODUYET_LDC = "03";
  public static final String TUCHOI_LDC = "04";
  public static final String DADUYET_LDC = "05";
  public static final String CHODUYET_TK = "06";
  public static final String CHODUYET_KTVBQ = "07";
  public static final String CHODUYET_KT = "08";
  public static final String TUCHOI_TK = "09";
  public static final String TUCHOI_KTVBQ = "10";
  public static final String TUCHOI_KT = "11";
  public static final String DADUYET_TK = "12";
  public static final String DADUYET_KTVBQ = "13";
  public static final String DADUYET_KT = "14";
  public static final String CHODUYET_LDCC = "15";
  public static final String TUCHOI_LDCC = "16";
  public static final String DADUYET_LDCC = "17";
  public static final String CHODUYET_LDV = "18";
  public static final String TUCHOI_LDV = "19";
  public static final String DADUYET_LDV = "20";
  public static final String CHODUYET_LDTC = "21";
  public static final String TUCHOI_LDTC = "22";
  public static final String DADUYET_LDTC = "23";
  public static final String CHUATONGHOP = "24";
  public static final String DATONGHOP = "25";
  public static final String CHUATAO_QD = "26";
  public static final String DADUTHAO_QD = "27";
  public static final String DABANHANH_QD = "28";
  public static final String BAN_HANH = "29";
  public static final String DAKY = "30";
  public static final String CHUATAOTOTRINH = "31";
  public static final String DATAOTOTRINH = "32";
  public static final String CHUACAPNHAT = "33";
  public static final String DANGCAPNHAT = "34";
  public static final String HOANTHANHCAPNHAT = "35";
  public static final String HUYTHAU = "36";
  public static final String TRUNGTHAU = "37";
  public static final String CHODUYET_TBP_TVQT = "38";
  public static final String TUCHOI_TBP_TVQT = "39";


  public static final String CHUA_THUC_HIEN = "43";
  public static final String DANG_THUC_HIEN = "44";
  public static final String DA_HOAN_THANH = "45";

  public static final String CHO_DUYET_BTC = "49";
  public static final String TU_CHOI_BTC = "50";
  public static final String DA_DUYET_BTC = "51";
  public static final String DA_TAO_CBV = "52";
  public static final String DA_DUYET_CBV = "53";
  public static final String TU_CHOI_CBV = "54";

  public static final String YC_CHICUC_PHANBO_DC = "59";
  public static final String DA_PHANBO_DC_CHODUYET_TBP_TVQT = "60";
  public static final String DA_PHANBO_DC_TUCHOI_TBP_TVQT = "61";
  public static final String DA_PHANBO_DC_CHODUYET_LDCC = "62";
  public static final String DA_PHANBO_DC_TUCHOI_LDCC = "63";
  public static final String DA_PHANBO_DC_DADUYET_LDCC = "64";
  public static final String DA_PHANBO_DC_CHODUYET_TP = "65";
  public static final String DA_PHANBO_DC_TUCHOI_TP = "66";
  public static final String DA_PHANBO_DC_CHODUYET_LDC = "67";
  public static final String DA_PHANBO_DC_TUCHOI_LDC = "68";
  public static final String DA_PHANBO_DC_DADUYET_LDC = "69";
  public static final String DANG_DUYET_CB_VU = "77";
  public static final String DANG_NHAP_DU_LIEU = "78";
  public static final String TU_CHOI_BAN_HANH_QD = "79";
  public static final String DA_LAP = "80";

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
  public static final String HET_HIEU_LUC = "00";
  public static final String CON_HIEU_LUC = "01";

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

  // Phương thức tổ chức triển khai mua bán trực tiếp
  public static final String CHAO_GIA = "01";
  public static final String UY_QUYEN = "02";
  public static final String MUA_LE = "03";

  public static final String BAN_LE = "03";

  public static final String DAU_TU = "DT";

  public static final String CUNG_CAP = "CC";

  public static final String CHU_DONG = "CD";

  public static final String PHE_DUYET_TRUOC = "PDT";

  // Điều chuyển nội bộ
  public static final String DIEU_CHUYEN = "DC";

  public static final String NHAN_DIEU_CHUYEN = "NDC";

  public static final String NHAN_DIEU_CHUYEN_TS = "NDCTS";
  public static final String DIEU_CHUYEN_TS = "DCTS";

  // Loai diều chuyển

  public static final String GIUA_2_CHI_CUC_TRONG_1_CUC = "CHI_CUC";
  public static final String GIUA_2_CUC_DTNN_KV = "CUC";
  public static final String DCNB = "DCNB";


  // Loai hàng hóa xuất khác
  public static final String LT_6_THANG = "LT6";
  public static final String VT_12_THANG = "VT12";
  public static final String VT_6_THANG = "VT6";
  public static final String NHAP_SAU_BH = "NHAP_SAU_BH";
  public static final String NHAP_MAU = "NHAP_MAU";

  public static final String QD_GNV = "QD_GNV";


  public static final Map<String, String> mappingLoaiDx;

  static {
    mappingLoaiDx = Maps.<String, String>buildMap().put(Contains.DX_THANH_LY, "Thanh lý")
        .put(Contains.DX_TIEU_HUY, "Tiêu hủy").get();
  }

  public static final Map<String, String> mappingPthucMtt;

  static {
    mappingPthucMtt = Maps.<String, String>buildMap().put(Contains.CHAO_GIA, "Chào giá")
        .put(Contains.UY_QUYEN, "Úy quyền").put(Contains.MUA_LE, "Mua lẻ").get();
  }

  public static String getPthucMtt(String key) {
    return Contains.mappingPthucMtt.get(key);
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

  public static String convertDateToString(Date date) throws Exception {
    if (Objects.isNull(date)) {
      return null;
    }
    DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE_STR);
    return df.format(date);
  }
  public static String convertDateToString(LocalDate localDate) {
    if (Objects.isNull(localDate)) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
    return formatter.format(localDate);
  }

  public static Page<T> convertListToPage(List<T> list, Pageable pageable) {
    final int start = (int) pageable.getOffset();
    final int end = Math.min((start + pageable.getPageSize()), list.size());
    final Page<T> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
    return page;
  }

  public static class NguoiLienQuan {
    public static String KHACH_MOI = "KM";
    public static String DAU_GIA_VIEN = "DGV";
    public static String NGUOI_THAM_GIA = "NTG";
  }

  public static class HO_SO_KY_THUAT {
    public static String BBAN_KTRA_NGOAI_QUAN = "BBKTNQ";
    public static String BB_KTRA_VAN_HANH = "BBKTVH";
    public static String BB_KTRA_HO_SO_KY_THUAT = "BBKTHSKT";
    public static String NGUOI_LIEN_QUAN = "NLQ";
    public static String HO_SO = "HS";
    public static String THOI_DIEM_NHAP_HANG = "NHAP";
    public static String THOI_DIEM_XUAT_HANG = "XUAT";
  }

  public static class BIEN_BAN_LAY_MAU {
    public static String NGUOI_LIEN_QUAN = "NLQ";
    public static String PHUONG_PHAP_LAY_MAU = "PPLM";
    public static String CHI_TIEU_CHAT_LUONG = "CTCL";
  }

  // Loai hình xuất
  public static final String XUAT_MAU = "XUAT_MAU";
  public static final String XUAT_HUY = "XUAT_HUY";
  public static final String XUAT_BH = "XUAT_BH";
  public static final Map<String, String> mapLoaiHinhXuat;

  static {
    mapLoaiHinhXuat = Maps.<String, String>buildMap().put(Contains.XUAT_MAU, "Xuất hàng để lấy mẫu")
        .put(Contains.XUAT_HUY, "Xuất hàng bị hủy khỏi kho")
        .put(Contains.XUAT_BH, "Xuất để bảo hành")
        .get();
  }

  public static String getLoaiHinhXuat(String key) {
    return Contains.mapLoaiHinhXuat.get(key);
  }
}
