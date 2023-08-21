package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdDtlReq {
    private Long id;
    private Long idHdr;
    private Long idDx;
    private String soDx;
    private String maDviDx;
    private LocalDate ngayPduyetDx;
    private String trichYeuDx;
    private BigDecimal tongSoLuongDx;
    private BigDecimal soLuongXuatCap;
    private BigDecimal thanhTienDx;
    private LocalDate ngayDx;
    private LocalDate ngayKetThucDx;
    private String type;
    private List<XhCtvtQuyetDinhPdDxReq> quyetDinhPdDx = new ArrayList<>();
}
