package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuKtChatLuongHdrDTO {
    private Long id;
    private Long bBNtLdId;
    private Long qDinhDccId;
    private String soQdinh;
    private String soBBNtLd;
    private LocalDate thoiGianDieuChuyen;
    private Integer nam;
    private String ketQuaDanhGia;
    private String maNhaKhoXuat;
    private String tenNhaKhoXuat;
    private String maDiemKhoXuat;
    private String tenDiemKhoXuat;
    private String maloKhoXuat;
    private String tenloKhoXuat;
    private String maNganKhoXuat;
    private String tenNganKhoXuat;
    private Boolean thayDoiThuKho;
    private String soPhieuKtChatLuong;
    private LocalDate ngayGiamDinh;
    private String ketQua;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private String maNhaKhoNhan;
    private String tenNhaKhoNhan;
    private String maDiemKhoNhan;
    private String tenDiemKhoNhan;
    private String maloKhoNhan;
    private String tenloKhoNhan;
    private String maNganKhoNhan;
    private String tenNganKhoNhan;

    private String trangThai;
    private String tenTrangThai;

}
