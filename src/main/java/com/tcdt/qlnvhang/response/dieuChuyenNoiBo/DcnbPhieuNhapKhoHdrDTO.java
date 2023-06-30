package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuNhapKhoHdrDTO {
    private Long id;
    private Long qdDcCucId;
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
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long bangKeVtId;
    private String soBangKeVt;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private String tenDonvitinh;
    private BigDecimal slDienChuyen;
    private BigDecimal duToanKinhPhiDc;

    private Long phieuKiemTraId;
    private String soPhieuKiemTraCl;
    private LocalDate ngayGiamDinh;

    private String trangThai;
    private String tenTrangThai;
}
