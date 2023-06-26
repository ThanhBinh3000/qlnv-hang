package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBBKetThucNKHdrDTO {
    private Long id;
    private Long qdDcCucId;
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
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private String tenDonViTinh;

    private String soBBKtNH;
    private LocalDate ngayKetThucNhapHang;

    private String soHoSoKyThuat;
    private Long hoSoKyThuatId;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;

    private String soBbLayMau;
    private Long bbLayMauId;
    private String soBbGuiHang;
    private Long bbGuiHangId;

    private String trangThai;
    private String tenTrangThai;
}
