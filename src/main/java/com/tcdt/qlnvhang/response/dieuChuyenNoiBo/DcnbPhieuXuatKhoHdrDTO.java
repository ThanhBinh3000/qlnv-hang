package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuXuatKhoHdrDTO {
//    Số QĐ ĐC của Cục	Năm KH	Thời hạn điều chuyển	Điểm kho	Lô kho	Số Phiếu xuất kho	Ngày xuất kho	Số phiếu KNCL	Ngày giám định	Trạng thái
    private Long id;
    private Long qDinhDccId;
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
    private Boolean thayDoiThuKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private String soPhieuKiemNghiemCl;
    private LocalDate ngayGiamDinh;
    private String trangThai;
    private String tenTrangThai;
}
