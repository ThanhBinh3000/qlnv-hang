package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBangKeXuatVTHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;

    private String soPhieuXuat;
    private String soBangKe;
    private LocalDate ngayXuatKho;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBangKeXuatVTHdrDTO(Long id, Long qDinhDccId, String soQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, String soPhieuXuat, String soBangKe, LocalDate ngayXuatKho, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maloKho = maloKho;
        this.tenloKho = tenloKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.soPhieuXuat = soPhieuXuat;
        this.soBangKe = soBangKe;
        this.ngayXuatKho = ngayXuatKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
