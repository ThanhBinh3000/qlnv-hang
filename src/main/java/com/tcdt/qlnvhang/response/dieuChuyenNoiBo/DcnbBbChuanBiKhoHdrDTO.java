package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBbChuanBiKhoHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiGianNhapKhoMuonNhat;
    private String soBbChuanBiKho;
    private LocalDate ngayBbChuanBiKho;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private Long bbKetThucNKId;
    private String soBbKetThucNK;
    private LocalDate ngayKtNhapKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private String trangThai;
    private String tenTrangThai;
}
