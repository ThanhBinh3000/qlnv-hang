package com.tcdt.qlnvhang.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdDtlDTO {
    private Long id;
    private Long idDx;
    private String soDx;
    private LocalDate ngayKyDx;
    private String trichYeuDx;
    private Long soLuongDx;
    private String loaiHinhNhapXuat;
    private String loaiNhapXuat;
    private String kieuNhapXuat;
    private String mucDichXuat;
    private String noiDungDx;
    private Long idDonViNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDvi;
    private Long soLuong;
    private Long soLuongXc;
    private Long soLuongNhuCauXuat;
    private Long tonKhoDvi;
    private Long tonKhoLoaiVthh;
    private Long tonKhoCloaiVthh;
    private String donViTinh;
    private Long idQdGnv;
    private String soQdGnv;
    private LocalDate ngayKetThuc;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String tenDvi;
    private List<XhCtvtQuyetDinhPdDtlDTO> childData;
}
