package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DcnbBangKeXuatVTHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngayKyQd;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;

    private String soPhieuXuat;
    private Long phieuXuatKhoId;
    private String soBangKe;
    private LocalDate ngayXuatKho;
    private String trangThai;
    private String tenTrangThai;
    private Long keHoachDcDtlId;
    public DcnbBangKeXuatVTHdrDTO(Long id, Long qDinhDccId, String soQdinh, LocalDate ngayKyQd, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String donViTinh, String soPhieuXuat, Long phieuXuatKhoId, String soBangKe, LocalDate ngayXuatKho, String trangThai, String tenTrangThai,Long keHoachDcDtlId) {
        this.id = id;
        this.qDinhDccId = qDinhDccId;
        this.soQdinh = soQdinh;
        this.ngayKyQd = ngayKyQd;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.donViTinh = donViTinh;
        this.soPhieuXuat = soPhieuXuat;
        this.phieuXuatKhoId = phieuXuatKhoId;
        this.soBangKe = soBangKe;
        this.ngayXuatKho = ngayXuatKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.keHoachDcDtlId= keHoachDcDtlId;
    }
}
