package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdGiaoNvNhangDtlReq {
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<HhQdGiaoNvNhDdiemReq> hhQdGiaoNvNhDdiemList = new ArrayList<>();
}
