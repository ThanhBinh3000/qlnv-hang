package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class DcnbPhieuKnChatLuongHdrDTO {
    private Long id;
    private Long bBLayMauId;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer nam;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private Boolean thayDoiThuKho;
    private String soPhieuKnChatLuong;
    private LocalDate ngayKiemNghiem;
    private String soBBLayMau;
    private LocalDate ngaylayMau;
    private String soBBTinhKho;
    private LocalDate ngayXuatDocKho;
    private String bbHaoDoi;
    private String trangThai;
    private String tenTrangThai;

}
