package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBangKeNhapVTHdrDTO {
    private Long id;
    private Long qDinhDcId;
    private String soQdinh;
    private LocalDate ngayKyQd;
    private Integer nam;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private String tenDonViTinh;

    private Long bBLayMauId;
    private String soBBLayMau;
    private String soBangKe;
    private String soBBGuiHang;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBangKeNhapVTHdrDTO(Long id, Long qDinhDcId, String soQdinh,LocalDate ngayKyQd, Integer nam, String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, String maLoKho, String tenLoKho,String maHangHoa,String tenHangHoa,String maChLoaiHangHoa,String tenChLoaiHangHoa,String donViTinh,String tenDonViTinh, Long bBLayMauId, String soBBLayMau, String soBangKe, String soBBGuiHang, String soPhieuNhapKho, Long phieuNhapKhoId, LocalDate ngayNhapKho, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qDinhDcId = qDinhDcId;
        this.soQdinh = soQdinh;
        this.ngayKyQd=ngayKyQd;
        this.nam = nam;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa=tenHangHoa;
        this.maChLoaiHangHoa=maChLoaiHangHoa;
        this.tenChLoaiHangHoa=tenChLoaiHangHoa;
        this.donViTinh=donViTinh;
        this.tenDonViTinh=tenDonViTinh;
        this.bBLayMauId = bBLayMauId;
        this.soBBLayMau = soBBLayMau;
        this.soBangKe = soBangKe;
        this.soBBGuiHang = soBBGuiHang;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.ngayNhapKho = ngayNhapKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
