package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
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
}
