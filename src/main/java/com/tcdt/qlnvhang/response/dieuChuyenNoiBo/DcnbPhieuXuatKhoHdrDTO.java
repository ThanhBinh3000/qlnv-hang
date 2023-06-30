package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuXuatKhoHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
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
    private Boolean thayDoiThuKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Long phieuKiemNghiemId;
    private String soPhieuKiemNghiemCl;
    private LocalDate ngayGiamDinh;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String ktvBaoQuan;
    private String donViTinh;
    private BigDecimal slDienChuyen;
    private BigDecimal duToanKinhPhiDc;
    private Long bKCanHangId;
    private String soBKCanHang;

    private String trangThai;
    private String tenTrangThai;
}
