package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBbTinhkBttDtlReq {
    private Long id;
    private Long idHdr;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Long idBangKeHang;
    private String soBangKeHang;
    private BigDecimal soLuongXuat;
}