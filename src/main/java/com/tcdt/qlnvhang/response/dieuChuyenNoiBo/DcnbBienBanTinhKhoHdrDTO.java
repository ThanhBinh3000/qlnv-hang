package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBienBanTinhKhoHdrDTO {
    private Long id;
    private Long qDinhDcId;
    private Long phieuXuatKhoId;
    private Long bangKeCanHangId;
    private String soQdinh;
    private Integer nam;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soBbTinhKho;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private String soPhieuXuatKho;
    private String soBangKeXuatDcLt;
    private LocalDate ngayXuatKho;
    private String trangThai;
    private String tenTrangThai;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayKyQdinh;

    public DcnbBienBanTinhKhoHdrDTO(Long id, Long qDinhDcId, Long phieuXuatKhoId, Long bangKeCanHangId, String soQdinh, Integer nam, LocalDate thoiHanDieuChuyen, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String soBbTinhKho, LocalDate ngayBatDauXuat, LocalDate ngayKetThucXuat, String soPhieuXuatKho, String soBangKeXuatDcLt, LocalDate ngayXuatKho, String trangThai, String tenTrangThai, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, LocalDate ngayHieuLuc, LocalDate ngayKyQdinh) {
        this.id = id;
        this.qDinhDcId = qDinhDcId;
        this.phieuXuatKhoId = phieuXuatKhoId;
        this.bangKeCanHangId = bangKeCanHangId;
        this.soQdinh = soQdinh;
        this.nam = nam;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.soBbTinhKho = soBbTinhKho;
        this.ngayBatDauXuat = ngayBatDauXuat;
        this.ngayKetThucXuat = ngayKetThucXuat;
        this.soPhieuXuatKho = soPhieuXuatKho;
        this.soBangKeXuatDcLt = soBangKeXuatDcLt;
        this.ngayXuatKho = ngayXuatKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.ngayHieuLuc = ngayHieuLuc;
        this.ngayKyQdinh = ngayKyQdinh;
    }
}
