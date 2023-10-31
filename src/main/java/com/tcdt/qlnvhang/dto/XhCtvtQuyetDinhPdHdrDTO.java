package com.tcdt.qlnvhang.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdHdrDTO {
    private String tenDvi;
    private String maDvi;
    private String soDx;
    private String trichYeuDx;
    private String mucDichXuat;
    private LocalDate ngayKetThuc;
    private LocalDate ngayKyDx;
    private LocalDate thoiGian;
    private Long soLuongDx;
    private Long soLuong;
    private Long soLuongNhuCauXuat;
    private Long soLuongXc;
    private List<XhCtvtQuyetDinhPdDtlDTO> childData;
}
