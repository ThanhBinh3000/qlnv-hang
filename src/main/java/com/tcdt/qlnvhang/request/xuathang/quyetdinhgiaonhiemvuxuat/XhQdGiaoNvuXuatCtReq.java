package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdGiaoNvuXuatCtReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal tonKho;
    private BigDecimal soLuongXuatBan;
    private String trangThai;
    private List<XhQdGiaoNvXhDdiemReq> children = new ArrayList<>();
}