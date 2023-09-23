package com.tcdt.qlnvhang.response.feign;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class KtMlkObject {
    String maNgankho;
    String tenNgankho;
    String diaChi;

    String namSudung;
    String nhiemVu;
    String ghiChu;
    String loaikhoId;
    String chatluongId;
    String tinhtrangId;
    String ngankhoHientrangId;
    BigDecimal dienTichDat;
    BigDecimal tichLuongTkLt;
    BigDecimal tichLuongTkVt;
    BigDecimal tichLuongChua;
    //  Long nhakhoId;
    Long quyhoachDuyetId;
    BigDecimal tichLuongChuaLt;
    BigDecimal tichLuongChuaVt;
    BigDecimal theTichChuaLt;
    BigDecimal tichLuongKhaDung;
    BigDecimal tichLuongKdLt;
    BigDecimal tichLuongKdVt;
    String huongSuDung;
    BigDecimal tichLuongKdLtvt;
    Integer trangThaiTl;
    Integer namNhap;
    BigDecimal tichLuongChuaLtGao;
    BigDecimal tichLuongChuaLtThoc;
    BigDecimal theTichChuaVt;
    BigDecimal theTichTkLt;
    BigDecimal theTichTkVt;
    BigDecimal theTichKdLt;
    BigDecimal theTichKdVt;

    BigDecimal thanhTien;

    Long updateStatus;
    String trangThai;
    String nguoiTao;
    String nguoiSua;
    String ldoTuchoi;
    String nguoiGuiDuyet;
    String nguoiPduyet;
    Boolean coLoKho;

    String loaiVthh;

    String cloaiVthh;

    BigDecimal slTon;

    String dviTinh;

    Long diemkhoId;

    Long tongkhoId;

    Long dtqgkvId;

    String loaiHangHoa;

    String kieuHang;
    String active;

    String maThuKho;

    Long idThuKho;

    String tenNhakho;

    String tenDiemkho;

    String tenTongKho;

    String tenDtkvqg;
    String tenLoaiVthh;

    String tenCloaiVthh;
}
