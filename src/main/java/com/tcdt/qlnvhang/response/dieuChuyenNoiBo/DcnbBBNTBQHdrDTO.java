package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class DcnbBBNTBQHdrDTO {
    private Long id;
    private Long qDinhDccId;
    private String soQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maDiemKhoXuat;
    private String tenDiemKhoXuat;
    private String maloKhoXuat;
    private String tenloKhoXuat;
//    private String trangThaiXuat;
//    private String tenTrangThaiXuat;
    private String maDiemKhoNhan;
    private String tenDiemKhoNhan;
    private String maloKhoNhan;
    private String tenloKhoNhan;
//    private String trangThaiNhan;
//    private String tenTrangThaiNhan;
    private Long soLapBBKLot;
    private LocalDate ngayLapBBKLot;
    private LocalDate ngayKetThucNtKeLot;
    private BigDecimal tongKinhPhiTT;
    private BigDecimal tongKinhPhiPd;
    private String trangThai;
    private String tenTrangThai;
}
