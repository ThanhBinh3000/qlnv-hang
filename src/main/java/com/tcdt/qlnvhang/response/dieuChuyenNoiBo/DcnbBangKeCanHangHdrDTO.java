package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBangKeCanHangHdrDTO {
    private Long id;
    private Long qDinhDcId;
    private Long phieuXuatKhoId;
    private String soQdinh;
    private Integer nam;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
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
