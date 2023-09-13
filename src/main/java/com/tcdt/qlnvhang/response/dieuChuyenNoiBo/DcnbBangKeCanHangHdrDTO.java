package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;

@Data
public class DcnbBangKeCanHangHdrDTO {
    private Long id;
    private Long qDinhDcId;
    private Long phieuXuatKhoId;
    private String soQdinh;
    private LocalDate ngayKyQd;
    private Integer nam;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soPhieuXuatKho;
    private String soBangKe;
    private LocalDate ngayXuatKho;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private String trangThai;
    private String tenTrangThai;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String maNhaKho;
    private String tenNhaKho;
    private String donViTinh;
    private String maNganKho;
    private String tenNganKho;
    private String nguoiGiaoHang;
    private String soCmt;
    private String ctyNguoiGh;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;

    public DcnbBangKeCanHangHdrDTO(Long id, Long qDinhDcId, Long phieuXuatKhoId, String soQdinh, LocalDate ngayKyQd, Integer nam, LocalDate thoiHanDieuChuyen, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String soPhieuXuatKho, String soBangKe, LocalDate ngayXuatKho, String soPhieuNhapKho, LocalDate ngayNhapKho, String trangThai, String tenTrangThai, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String maNhaKho, String tenNhaKho, String donViTinh, String maNganKho, String tenNganKho, String nguoiGiaoHang, String soCmt, String ctyNguoiGh, String diaChi, LocalDate thoiGianGiaoNhan) {
        this.id = id;
        this.qDinhDcId = qDinhDcId;
        this.phieuXuatKhoId = phieuXuatKhoId;
        this.soQdinh = soQdinh;
        this.ngayKyQd = ngayKyQd;
        this.nam = nam;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.soPhieuXuatKho = soPhieuXuatKho;
        this.soBangKe = soBangKe;
        this.ngayXuatKho = ngayXuatKho;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.donViTinh = donViTinh;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.nguoiGiaoHang = nguoiGiaoHang;
        this.soCmt = soCmt;
        this.ctyNguoiGh = ctyNguoiGh;
        this.diaChi = diaChi;
        this.thoiGianGiaoNhan = thoiGianGiaoNhan;
    }
}
