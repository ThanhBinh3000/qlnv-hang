package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
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

    public DcnbPhieuKtChatLuongHdrDTO(Long id, Long bBNtLdId, Long qDinhDccId, String soQdinh, String soBBNtLd, LocalDate thoiGianDieuChuyen, Integer nam, String ketQuaDanhGia, String maNhaKhoXuat, String tenNhaKhoXuat, String maDiemKhoXuat, String tenDiemKhoXuat, String maloKhoXuat, String tenloKhoXuat, String maNganKhoXuat, String tenNganKhoXuat, Boolean thayDoiThuKho, String soPhieuKtChatLuong, LocalDate ngayGiamDinh, String ketQua, String soPhieuNhapKho, LocalDate ngayNhapKho, String maNhaKhoNhan, String tenNhaKhoNhan, String maDiemKhoNhan, String tenDiemKhoNhan, String maloKhoNhan, String tenloKhoNhan, String maNganKhoNhan, String tenNganKhoNhan, String trangThai, String tenTrangThai) {
        this.id = id;
        this.bBNtLdId = bBNtLdId;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.soBBNtLd = soBBNtLd;
        this.thoiGianDieuChuyen = thoiGianDieuChuyen;
        this.nam = nam;
        this.ketQuaDanhGia = ketQuaDanhGia;
        this.maNhaKhoXuat = maNhaKhoXuat;
        this.tenNhaKhoXuat = tenNhaKhoXuat;
        this.maDiemKhoXuat = maDiemKhoXuat;
        this.tenDiemKhoXuat = tenDiemKhoXuat;
        this.maloKhoXuat = maloKhoXuat;
        this.tenloKhoXuat = tenloKhoXuat;
        this.maNganKhoXuat = maNganKhoXuat;
        this.tenNganKhoXuat = tenNganKhoXuat;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soPhieuKtChatLuong = soPhieuKtChatLuong;
        this.ngayGiamDinh = ngayGiamDinh;
        this.ketQua = ketQua;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.maNhaKhoNhan = maNhaKhoNhan;
        this.tenNhaKhoNhan = tenNhaKhoNhan;
        this.maDiemKhoNhan = maDiemKhoNhan;
        this.tenDiemKhoNhan = tenDiemKhoNhan;
        this.maloKhoNhan = maloKhoNhan;
        this.tenloKhoNhan = tenloKhoNhan;
        this.maNganKhoNhan = maNganKhoNhan;
        this.tenNganKhoNhan = tenNganKhoNhan;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
