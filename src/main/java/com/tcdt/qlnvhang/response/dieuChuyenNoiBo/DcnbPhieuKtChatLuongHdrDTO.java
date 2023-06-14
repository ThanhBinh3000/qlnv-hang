package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuKtChatLuongHdrDTO {
    private Long id;
    private Long bBNtLdId;
    private Long qDinhDccId;
    private String soQdinh;
    private String soBBNtLd;
    private LocalDate ngayGiamDinh;
    private Integer nam;
    private String ketQuaDanhGia;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private Boolean thayDoiThuKho;
    private String soPhieuKNChatLuong;
    private LocalDate ngayKiemNghiem;
    private LocalDate ngayXuatDocKho;
    private String trangThai;
    private String tenTrangThai;
}
